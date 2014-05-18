package batch;

import batch.wiring.BatchConfig;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpringBatchDumplingApp {
    private static Logger log = LoggerFactory.getLogger(SpringBatchDumplingApp.class);

    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;

    public SpringBatchDumplingApp(JobLauncher jobLauncher, JobExplorer jobExplorer) {
        this.jobLauncher = jobLauncher;
        this.jobExplorer = jobExplorer;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BatchConfig.class);
        Lists.newArrayList(context.getBeanDefinitionNames()).forEach(log::info);
        SpringBatchDumplingApp app = context.getBean(SpringBatchDumplingApp.class);
        Job dumplingDeliveryJob = context.getBean("dumplingDeliveryJob", Job.class);
        app.runJob(dumplingDeliveryJob);
    }

    private void runJob(Job job) {
        Map<String, JobParameter> params = new HashMap<>();
        params.put("nanos", new JobParameter(System.nanoTime()));
        JobParameters guaranteedNewJobParameters = new JobParameters(params);
        try {
            JobExecution jobExecution = jobLauncher.run(job, guaranteedNewJobParameters);
            while(stillRunning(jobExecution.getExitStatus())) {
                log.info(jobExecution.getExitStatus().toString());
                jobExecution = jobExplorer.getJobExecution(jobExecution.getId());
                jobExecution.getStepExecutions().forEach(se -> log.info(se.getSummary()));
                Thread.sleep(100);
            }
            log.info(jobExecution.getExitStatus().toString());
            jobExecution = jobExplorer.getJobExecution(jobExecution.getId());
            jobExecution.getStepExecutions().forEach(se -> log.info(se.getSummary()));
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException | JobParametersInvalidException | JobRestartException | InterruptedException e) {
            log.error(e.getMessage());
        }

    }

    private boolean stillRunning(ExitStatus exitStatus) {
        return !ExitStatus.COMPLETED.equals(exitStatus) && !ExitStatus.FAILED.equals(exitStatus) && !ExitStatus.STOPPED.equals(exitStatus);
    }
}
