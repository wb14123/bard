package com.bardframework.bard.basic.injector;

import com.bardframework.bard.basic.GenericTester;
import com.bardframework.bard.basic.marker.MultipartParam;
import com.bardframework.bard.core.Handler;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;

import javax.servlet.ServletException;
import javax.ws.rs.Path;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class MultipartParamInjectorTest extends GenericTester {

    @Test
    public void testMultipart() throws IOException, ServletException {
        String partName = "file";
        String resourceName = "/testFile.txt";

        byte[] fileContent = FileCopyUtils.copyToByteArray(
            getClass().getResourceAsStream(resourceName));
        // Create part & entity from resource
        Part[] parts = new Part[] {
            new FilePart(partName, new ByteArrayPartSource(resourceName, fileContent))};
        MultipartRequestEntity multipartRequestEntity =
            new MultipartRequestEntity(parts, new PostMethod().getParams());
        // Serialize request body
        ByteArrayOutputStream requestContent = new ByteArrayOutputStream();
        multipartRequestEntity.writeRequest(requestContent);
        // Set request body to HTTP servlet request
        request.setContent(requestContent.toByteArray());
        // Set content type to HTTP servlet request (important, includes Mime boundary string)
        request.setContentType(multipartRequestEntity.getContentType());

        request.setPathInfo("/multipart-test");
        request.setMethod("POST");

        servlet.service(request, response);
        assertEquals(resourceName, response.getHeader("file-name"));
        assertEquals("This is a file for multipart test.\n", response.getHeader("file-content"));
    }

    public static class MultipartTest extends Handler {
        @Path("/multipart-test")
        public void test(
            @MultipartParam("file") FileItem file
        ) throws IOException {
            context.getResponse().setHeader("file-name", file.getName());
            InputStream fileInput = file.getInputStream();
            String fileContent = IOUtils.toString(fileInput);
            context.getResponse().setHeader("file-content", fileContent);
        }
    }

}
