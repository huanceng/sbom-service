package org.openeuler.sbom.analyzer.vcs.apis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.openeuler.sbom.analyzer.model.RepoInfo;
import org.openeuler.sbom.analyzer.vcs.AbstractVcsApiFactory;
import org.openeuler.sbom.analyzer.vcs.VcsApi;
import org.openeuler.sbom.analyzer.vcs.VcsEnum;
import org.openeuler.sbom.analyzer.vcs.VcsToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class GiteeApi implements VcsApi {
    public static class Factory extends AbstractVcsApiFactory<GiteeApi> {
        public Factory() {
            super(VcsEnum.GITEE);
        }

        @Override
        public GiteeApi create() {
            return GiteeApi.getInstance();
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(GiteeApi.class);

    private static volatile GiteeApi instance = null;

    private GiteeApi() {}

    public static GiteeApi getInstance() {
        if (instance == null) {
            synchronized (GiteeApi.class) {
                if (instance == null) {
                    instance = new GiteeApi();
                }
            }
        }
        return instance;
    }

    private final GiteeApiService service = GiteeApiService.create();

    @Override
    public RepoInfo getRepoInfo(String org, String repo) {
        logger.info("start to get repo info from {} for [org: '{}', repo: '{}']", VcsEnum.GITEE, org, repo);
        GiteeApiService.RepoInfo repoInfo = new GiteeApiService.RepoInfo();

        Map<String, String> headers = new HashMap<>();
        String token = VcsToken.getToken(VcsEnum.GITEE);
        if (StringUtils.isNotEmpty(token)) {
            headers.put("Authorization", "token %s".formatted(token));
        }

        try {
            repoInfo = Optional.ofNullable(service.getRepoInfo(org, repo, headers).execute().body())
                    .orElse(new GiteeApiService.RepoInfo());
        } catch (IOException e) {
            logger.warn("failed to get repo info from {} for [org: '{}', repo: '{}']", VcsEnum.GITEE, org, repo, e);
        }

        SortedSet<String> authors = new TreeSet<>(Set.of(Optional.ofNullable(repoInfo.owner())
                .orElse(new GiteeApiService.Owner("")).login()));
        SortedSet<String> licenses = new TreeSet<>(Set.of(Optional.ofNullable(repoInfo.license()).orElse("")));
        return new RepoInfo(authors, licenses,
                Optional.ofNullable(repoInfo.description()).orElse(""),
                Optional.ofNullable(repoInfo.homepage()).orElse(""),
                Optional.ofNullable(repoInfo.repoUrl()).orElse(""));
    }

    interface GiteeApiService {
        String API_URL = "https://gitee.com/";

        static GiteeApi.GiteeApiService create() {
            OkHttpClient giteeApiClient = new OkHttpClient().newBuilder().addInterceptor(chain -> {
                Request request = chain.request();
                Request.Builder requestBuilder = request.newBuilder();
                return chain.proceed(requestBuilder.build());
            }).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(giteeApiClient)
                    .baseUrl(API_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            return retrofit.create(GiteeApi.GiteeApiService.class);
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        record Owner(String login) implements Serializable {}


        @JsonIgnoreProperties(ignoreUnknown = true)
        record RepoInfo(Owner owner, String license, String description, String homepage,
                        @JsonProperty("html_url") String repoUrl) implements Serializable {
            RepoInfo() {
                this(new Owner(""), "", "", "", "");
            }
        }

        @GET("api/v5/repos/{org}/{repo}")
        Call<RepoInfo> getRepoInfo(@Path("org") String org, @Path("repo") String repo,
                                   @HeaderMap Map<String, String> headers);
    }
}
