package org.binwang.bard.basic;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.binwang.bard.basic.marker.MultipartParam;
import org.binwang.bard.core.Handler;
import org.binwang.bard.core.Servlet;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.FileCopyUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class MultipartParamInjectorTest {

    public Servlet servlet = null;
    public MockHttpServletRequest request;
    public HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        servlet = new Servlet("org.binwang.bard.basic");
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

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
            context.response.setHeader("file-name", file.getName());
            InputStream fileInput = file.getInputStream();
            String fileContent = IOUtils.toString(fileInput);
            context.response.setHeader("file-content", fileContent);
        }
    }

}
