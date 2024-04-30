package com.muzimin.job;

import com.muzimin.configuration.Config;
import com.muzimin.utils.ConfigKeyName;
import com.muzimin.utils.EnvironmentUtil;
import com.muzimn.JobMode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.runtime.state.StateBackend;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableConfig;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.util.TernaryBoolean;

import static com.muzimn.JobMode.*;

/**
 * @author: 李煌民
 * @date: 2024-04-30 14:51
 **/
@Slf4j
@Data
public class Job {
    private StreamExecutionEnvironment environment;
    private StreamTableEnvironment tableEnvironment;
    private Config config;
    private String jobMode;

    public Job(Config config) {
        this.config = config;
        this.jobMode = config.getEnv().getJobMode();
        createStreamEnvironment();
        createStreamTableEnvironment();
    }

    private void createStreamTableEnvironment() {
        EnvironmentSettings environmentSettings = EnvironmentSettings.newInstance().inStreamingMode().build();
        tableEnvironment = StreamTableEnvironment.create(environment, environmentSettings);
    }

    private void createStreamEnvironment() {
        Configuration configuration = new Configuration();
        EnvironmentUtil.initConfiguration(config, configuration);
        environment = StreamExecutionEnvironment.getExecutionEnvironment(configuration);
        setTimeCharacteristic();
        setCheckpoint();
        EnvironmentUtil.setRestartStrategy(config, environment.getConfig());

        Integer parallelism = (Integer) config.getEnv().getParams().get("parallelism");
        if (parallelism != null) {
            environment.setParallelism(parallelism);
        }

        if (jobMode.equals(BATCH.toString())) {
            environment.setRuntimeMode(RuntimeExecutionMode.BATCH);
        }
    }

    private void setCheckpoint() {
        if (jobMode.equals(BATCH.toString())) {
            log.warn(
                    "Disabled Checkpointing. In flink execution environment, checkpointing is not supported and not needed when executing jobs in BATCH mode");
        }
        long interval = 0;
        /*if (config.hasPath(EnvCommonOptions.CHECKPOINT_INTERVAL.key())) {
            interval = config.getLong(EnvCommonOptions.CHECKPOINT_INTERVAL.key());
        } else if (config.hasPath(ConfigKeyName.CHECKPOINT_INTERVAL)) {
            log.warn(
                    "the parameter 'execution.checkpoint.interval' will be deprecated, please use common parameter 'checkpoint.interval' to set it");
            interval = config.getLong(ConfigKeyName.CHECKPOINT_INTERVAL);
        }

        if (interval > 0) {
            CheckpointConfig checkpointConfig = environment.getCheckpointConfig();
            environment.enableCheckpointing(interval);

            if (EnvironmentUtil.hasPathAndWaring(config, ConfigKeyName.CHECKPOINT_MODE)) {
                String mode = config.getString(ConfigKeyName.CHECKPOINT_MODE);
                switch (mode.toLowerCase()) {
                    case "exactly-once":
                        checkpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
                        break;
                    case "at-least-once":
                        checkpointConfig.setCheckpointingMode(CheckpointingMode.AT_LEAST_ONCE);
                        break;
                    default:
                        log.warn(
                                "set checkpoint.mode failed, unknown checkpoint.mode [{}],only support exactly-once,at-least-once",
                                mode);
                        break;
                }
            }

            if (EnvironmentUtil.hasPathAndWaring(
                    config, EnvCommonOptions.CHECKPOINT_TIMEOUT.key())) {
                long timeout = config.getLong(EnvCommonOptions.CHECKPOINT_TIMEOUT.key());
                checkpointConfig.setCheckpointTimeout(timeout);
            } else if (EnvironmentUtil.hasPathAndWaring(config, ConfigKeyName.CHECKPOINT_TIMEOUT)) {
                long timeout = config.getLong(ConfigKeyName.CHECKPOINT_TIMEOUT);
                checkpointConfig.setCheckpointTimeout(timeout);
            }

            if (EnvironmentUtil.hasPathAndWaring(config, ConfigKeyName.CHECKPOINT_DATA_URI)) {
                String uri = config.getString(ConfigKeyName.CHECKPOINT_DATA_URI);
                StateBackend fsStateBackend = new FsStateBackend(uri);
                if (EnvironmentUtil.hasPathAndWaring(config, ConfigKeyName.STATE_BACKEND)) {
                    String stateBackend = config.getString(ConfigKeyName.STATE_BACKEND);
                    if ("rocksdb".equalsIgnoreCase(stateBackend)) {
                        StateBackend rocksDBStateBackend =
                                new RocksDBStateBackend(fsStateBackend, TernaryBoolean.TRUE);
                        environment.setStateBackend(rocksDBStateBackend);
                    }
                } else {
                    environment.setStateBackend(fsStateBackend);
                }
            }

            if (EnvironmentUtil.hasPathAndWaring(
                    config, ConfigKeyName.MAX_CONCURRENT_CHECKPOINTS)) {
                int max = config.getInt(ConfigKeyName.MAX_CONCURRENT_CHECKPOINTS);
                checkpointConfig.setMaxConcurrentCheckpoints(max);
            }

            if (EnvironmentUtil.hasPathAndWaring(config, ConfigKeyName.CHECKPOINT_CLEANUP_MODE)) {
                boolean cleanup = config.getBoolean(ConfigKeyName.CHECKPOINT_CLEANUP_MODE);
                if (cleanup) {
                    checkpointConfig.enableExternalizedCheckpoints(
                            CheckpointConfig.ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION);
                } else {
                    checkpointConfig.enableExternalizedCheckpoints(
                            CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
                }
            }

            if (EnvironmentUtil.hasPathAndWaring(
                    config, ConfigKeyName.MIN_PAUSE_BETWEEN_CHECKPOINTS)) {
                long minPause = config.getLong(ConfigKeyName.MIN_PAUSE_BETWEEN_CHECKPOINTS);
                checkpointConfig.setMinPauseBetweenCheckpoints(minPause);
            }

            if (EnvironmentUtil.hasPathAndWaring(
                    config, ConfigKeyName.FAIL_ON_CHECKPOINTING_ERRORS)) {
                int failNum = config.getInt(ConfigKeyName.FAIL_ON_CHECKPOINTING_ERRORS);
                checkpointConfig.setTolerableCheckpointFailureNumber(failNum);
            }
        }*/
    }

    private void setTimeCharacteristic() {
        String timeType = (String) config.getEnv().getParams().get(ConfigKeyName.TIME_CHARACTERISTIC);
        if (timeType != null) {
            switch (timeType.toLowerCase()) {
                case "event-time":
                    environment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
                    break;
                case "ingestion-time":
                    environment.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime);
                    break;
                case "processing-time":
                    environment.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
                    break;
                default:
                    log.warn(
                            "set time-characteristic failed, unknown time-characteristic [{}],only support event-time,ingestion-time,processing-time",
                            timeType);
                    break;
            }
        }
    }
}
