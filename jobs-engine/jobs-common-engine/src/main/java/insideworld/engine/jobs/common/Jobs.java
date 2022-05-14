package insideworld.engine.jobs.common;

public interface Jobs {

    void start(String alias);

    void stop(String alias);

    void launchAll();

}
