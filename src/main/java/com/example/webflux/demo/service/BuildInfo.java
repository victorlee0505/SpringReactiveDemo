package com.example.webflux.demo.service;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BuildInfo {

    /** "git.branch". */
    public static final String GIT_BRANCH = "git.branch";
    /** "git.build.time". */
    public static final String GIT_BUILD_TIME = "git.build.time";
    /** "git.commit.id.abbrev". */
    public static final String GIT_COMMIT_ID_ABBREV = "git.commit.id.abbrev";
    /** "git.tags". */
    public static final String GIT_TAGS = "git.tags";

    /**
     * 
     * @return Map of git properties
     */
    public SortedMap<String, String> loadGitProp() {

        final TreeMap<String, String> gm = new TreeMap<>();
        try (InputStream is = this.getClass().getResourceAsStream("/git-demo.properties");
                var bis = new BufferedInputStream(is)) {
            final var prop = new Properties();
            prop.load(bis);
            prop.entrySet().forEach(s -> gm.put((String) s.getKey(), (String) s.getValue()));
        } catch (Exception e) {
            log.error("Failed to log build info[{}].", e);
        }
        return gm;
    }
    
    /**
     * get the current git tag, commit id, VCMS DB connection status, and ODS health
     * 
     * @param dbHealth
     * @return String of git tag, commit id, DB health, and ODS health
     */
    public String getVersionString() {
        final Map<String, String> gitProp = loadGitProp();
        var sb = new StringBuilder();
        sb.append(System.lineSeparator());
        sb.append("WEBFLUX-DEMO-API: ");
        sb.append("git.tag: ");
        sb.append(gitProp.get(BuildInfo.GIT_TAGS));
        sb.append(" / git.commit: ");
        sb.append(gitProp.get(BuildInfo.GIT_COMMIT_ID_ABBREV));
        sb.append(System.lineSeparator());

        return sb.toString();
    }
    
}
