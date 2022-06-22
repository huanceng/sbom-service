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

public class GithubApi implements VcsApi {
    public static class Factory extends AbstractVcsApiFactory<GithubApi> {
        public Factory() {
            super(VcsEnum.GITHUB);
        }

        @Override
        public GithubApi create() {
            return GithubApi.getInstance();
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(GithubApi.class);

    private static volatile GithubApi instance = null;

    private GithubApi() {}

    public static GithubApi getInstance() {
        if (instance == null) {
            synchronized (GithubApi.class) {
                if (instance == null) {
                    instance = new GithubApi();
                }
            }
        }
        return instance;
    }

    private final GithubApiService service = GithubApiService.create();

    @Override
    public RepoInfo getRepoInfo(String org, String repo) {
        logger.info("start to get repo info from {} for [org: '{}', repo: '{}']", VcsEnum.GITHUB, org, repo);
        GithubApiService.RepoInfo repoInfo = new GithubApiService.RepoInfo();

        Map<String, String> headers = new HashMap<>();
        String token = VcsToken.getToken(VcsEnum.GITHUB);
        if (StringUtils.isNotEmpty(token)) {
            headers.put("Authorization", "token %s".formatted(token));
        }

        try {
            repoInfo = Optional.ofNullable(service.getRepoInfo(org, repo, headers).execute().body())
                    .orElse(new GithubApiService.RepoInfo());
        } catch (IOException e) {
            logger.warn("failed to get repo info from {} for [org: '{}', repo: '{}']", VcsEnum.GITHUB, org, repo, e);
        }

        SortedSet<String> authors = new TreeSet<>(Set.of(Optional.ofNullable(repoInfo.owner())
                .orElse(new GithubApiService.Owner("")).login()));
        SortedSet<String> licenses = new TreeSet<>(Set.of(Optional.ofNullable(repoInfo.license())
                .orElse(new GithubApiService.License("")).name()));
        return new RepoInfo(authors, licenses,
                Optional.ofNullable(repoInfo.description()).orElse(""),
                Optional.ofNullable(repoInfo.homepage()).orElse(""),
                Optional.ofNullable(repoInfo.repoUrl()).orElse(""));
    }

    interface GithubApiService {
        String API_URL = "https://api.github.com/";

        static GithubApiService create() {
            OkHttpClient githubApiClient = new OkHttpClient().newBuilder().addInterceptor(chain -> {
                Request request = chain.request();
                Request.Builder requestBuilder = request.newBuilder();
                return chain.proceed(requestBuilder.build());
            }).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(githubApiClient)
                    .baseUrl(API_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            return retrofit.create(GithubApiService.class);
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        record Owner(String login) implements Serializable {}

        @JsonIgnoreProperties(ignoreUnknown = true)
        record License(String name) implements Serializable {}

        @JsonIgnoreProperties(ignoreUnknown = true)
        record RepoInfo(Owner owner, License license, String description, String homepage,
                        @JsonProperty("clone_url") String repoUrl) implements Serializable {
            RepoInfo() {
                this(new Owner(""), new License(""), "", "", "");
            }
        }

        @GET("repos/{org}/{repo}")
        Call<RepoInfo> getRepoInfo(@Path("org") String org, @Path("repo") String repo,
                                   @HeaderMap Map<String, String> headers);
    }
}
