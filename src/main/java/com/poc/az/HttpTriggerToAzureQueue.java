package com.poc.az;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.azure.functions.annotation.QueueOutput;

import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class HttpTriggerToAzureQueue {
    /**
     * This function listens at endpoint "/api/HttpTriggerToAzureQueue". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpTriggerToAzureQueue
     * 2. curl {your host}/api/HttpTriggerToAzureQueue?name=HTTP%20Query
     */
    @FunctionName("HttpTriggerToAzureQueue")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION)
            HttpRequestMessage<Optional<String>> request,
            @QueueOutput(name = "msg", queueName = "raw-queue", connection = "AzureWebJobsStorage")
            OutputBinding<String> msg,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        String query = request.getQueryParameters().get("name");
        String name = request.getBody().orElse(query);

        if (name == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        } else {
            //write the name to the message queue
            msg.setValue(name);
            return request.createResponseBuilder(HttpStatus.OK).body("Posted the message, " + name).build();
        }

    }
}
