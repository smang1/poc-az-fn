package com.poc.az;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.QueueOutput;
import com.microsoft.azure.functions.annotation.QueueTrigger;

/**
 * Azure Functions with Azure Storage Queue trigger.
 */
public class QueueTriggerFunction {
    /**
     * This function will be invoked when a new message is received at the specified path. The message contents are provided as input to this function.
     */
    @FunctionName("QueueTrigger-Java")
    public void run(
        @QueueTrigger(name = "inMsg", queueName = "raw-queue", connection = "AzureWebJobsStorage")
        String inMsg,
        @QueueOutput(name = "outMsg", queueName = "lake-queue", connection = "AzureWebJobsStorage")
        OutputBinding<Integer> outMsg,
        final ExecutionContext context
    ) {
        context.getLogger().info("Java Queue trigger function processing a message: " + inMsg);

        // Parse query parameter

        if (inMsg == null) {
            context.getLogger().info("No messages remaining in the queue");
        } else {
            //write the name to the message queue
            outMsg.setValue(inMsg.length());
            context.getLogger().info("Java Queue trigger function posted the message size to output queue");
        }
    }
}
