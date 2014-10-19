package org.binwang.bard.basic.injector;

import com.github.drapostolos.typeparser.TypeParser;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.binwang.bard.basic.marker.MultipartParam;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Injector;
import org.binwang.bard.core.marker.Before;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@BindTo(MultipartParam.class)
public class MultipartParamInjector extends Injector<MultipartParam> {
    @Before
    public void getMultipart() throws FileUploadException {
        Map<String, FileItem> fileMap = (Map<String, FileItem>) context.custom.get("multipart");
        if (fileMap == null) {
            fileMap = new HashMap<>();
            boolean isMultipart = ServletFileUpload.isMultipartContent(context.request);
            if (!isMultipart) {
                return;
            }

            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletContext servletContext = context.request.getServletContext();
            File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
            factory.setRepository(repository);
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(context.request);
            for (FileItem item : items) {
                fileMap.put(item.getFieldName(), item);
            }
        }

        FileItem item = fileMap.get(annotation.value());
        if (item != null && item.isFormField()) {
            TypeParser parser = TypeParser.newBuilder().build();
            String param = item.getString();
            injectorVariable = parser.parse(param, injectorVariableType);
        } else {
            injectorVariable = item;
        }
    }


    @Override public void generateDoc() {
    }
}
