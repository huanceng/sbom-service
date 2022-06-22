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

public class GitlabApi implements VcsApi {
    public static class Factory extends AbstractVcsApiFactory<GitlabApi> {
        public Factory() {
            super(VcsEnum.GITLAB);
        }

        @Override
        public GitlabApi create() {
            return GitlabApi.getInstance();
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(GitlabApi.class);

    private static volatile GitlabApi instance = null;

    private GitlabApi() {
    }

    public static GitlabApi getInstance() {
        if (instance == null) {
            synchronized (GitlabApi.class) {
                if (instance == null) {
                    instance = new GitlabApi();
                }
            }
        }
        return instance;
    }

    private final GitlabApiService service = GitlabApiService.create();

    @Override
    public RepoInfo getRepoInfo(String org, String repo) {
        logger.info("start to get repo info from {} for [org: '{}', repo: '{}']", VcsEnum.GITLAB, org, repo);
        GitlabApiService.RepoInfo repoInfo = new GitlabApiService.RepoInfo();

        Map<String, String> headers = new HashMap<>();
        String token = VcsToken.getToken(VcsEnum.GITLAB);
        if (StringUtils.isNotEmpty(token)) {
            headers.put("Authorization", "Bearer %s".formatted(token));
        }

        try {
            repoInfo = Optional.ofNullable(service.getRepoInfo(org, repo, headers).execute().body())
                    .orElse(new GitlabApiService.RepoInfo());
        } catch (IOException e) {
            logger.warn("failed to get repo info from {} for [org: '{}', repo: '{}']", VcsEnum.GITLAB, org, repo, e);
        }

        SortedSet<String> authors = new TreeSet<>(Set.of(Optional.ofNullable(repoInfo.owner())
                .orElse(new GitlabApiService.Owner("")).name()));
        SortedSet<String> licenses = new TreeSet<>(Set.of(Optional.ofNullable(repoInfo.license())
                .orElse(new GitlabApiService.License("")).name()));
        return new RepoInfo(authors, licenses,
                Optional.ofNullable(repoInfo.description()).orElse(""),
                Optional.ofNullable(repoInfo.homepage()).orElse(""),
                Optional.ofNullable(repoInfo.repoUrl()).orElse(""));
    }

    interface GitlabApiService {
        String API_URL = "https://gitlab.com/";

        static GitlabApiService create() {
            OkHttpClient gitlabApiClient = new OkHttpClient().newBuilder().addInterceptor(chain -> {
                Request request = chain.request();
                Request.Builder requestBuilder = request.newBuilder();
                return chain.proceed(requestBuilder.build());
            }).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(gitlabApiClient)
                    .baseUrl(API_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            return retrofit.create(GitlabApiService.class);
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        record Owner(String name) implements Serializable {
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        record License(String name) implements Serializable {
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        record RepoInfo(Owner owner, License license, String description, @JsonProperty("web_url") String homepage,
                        @JsonProperty("http_url_to_repo") String repoUrl) implements Serializable {
            RepoInfo() {
                this(new Owner(""), new License(""), "", "", "");
            }
        }

        @GET("api/v4/projects/{org}%2f{repo}?license=true")
        Call<RepoInfo> getRepoInfo(@Path("org") String org, @Path("repo") String repo,
                                   @HeaderMap Map<String, String> headers);
    }
}
