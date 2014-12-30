package com.bardframework.bard.basic.injector;

import com.bardframework.bard.basic.marker.ErrorCase;
import com.bardframework.bard.basic.marker.HandleErrors;
import com.bardframework.bard.basic.marker.MultipartParam;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Injector;
import com.bardframework.bard.core.Util;
import com.bardframework.bard.core.marker.Before;
import com.github.drapostolos.typeparser.TypeParser;
import com.github.drapostolos.typeparser.TypeParserException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@BindTo(MultipartParam.class)
public class MultipartParamInjector extends Injector<MultipartParam> {
    @Before
    @HandleErrors({
        @ErrorCase(code = 400, exception = TypeParserException.class, description = "params error" )
    })
    public void getMultipart() throws FileUploadException {
        context.putCustom("param" , annotation.value());
        Map<String, FileItem> fileMap = context.getCustom("multipart" );
        if (fileMap == null) {
            fileMap = new HashMap<>();
            boolean isMultipart = ServletFileUpload.isMultipartContent(context.getRequest());
            if (!isMultipart) {
                return;
            }

            DiskFileItemFactory factory = new DiskFileItemFactory();
            File repository;
            String pathName = Util.getConfig().getString("bard.upload.tempdir" , "/tmp" );
            repository = new File(pathName);
            factory.setRepository(repository);
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(context.getRequest());
            for (FileItem item : items) {
                fileMap.put(item.getFieldName(), item);
            }
            context.putCustom("multipart" , fileMap);
        }

        FileItem item = fileMap.get(annotation.value());
        if (item != null && item.isFormField()) {
            TypeParser parser = TypeParser.newBuilder().build();
            String param = item.getString();
            context.setInjectorVariable(parser.parse(param, context.getInjectorVariableType()));
        } else {
            context.setInjectorVariable(item);
        }
    }


    @Override public void generateDoc() {
        docParameter.name = annotation.value();
        docParameter.type = context.getInjectorVariableType();
        docParameter.belongs = "multipart";
    }
}
