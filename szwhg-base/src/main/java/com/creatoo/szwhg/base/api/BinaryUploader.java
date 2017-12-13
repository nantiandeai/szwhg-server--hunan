package com.creatoo.szwhg.base.api;

import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.FileType;
import com.baidu.ueditor.define.State;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Administrator
 */

@Component
public class BinaryUploader {
	@Value("${upload.dir}")
	public static String rootDir = "/home/scity/file-storage";

	public static final State save(@Context HttpServletRequest request, Map<String, Object> conf) {
//		FileItemStream fileStream = null;
		boolean isAjaxUpload = request.getHeader( "X_Requested_With" ) != null;
		if (!ServletFileUpload.isMultipartContent(request)) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}
		ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
		if ( isAjaxUpload ) {
			upload.setHeaderEncoding( "UTF-8" );
		}
		try {
			String id = UUID.randomUUID().toString();
			String relativeDir="pic/"+id.substring(0,1)+"/"+id.substring(1,2)+"/"+id.substring(2,3)+"/"+id.substring(3,4);
			String originFileName = id+".jpg";
			File targetDir = new File(rootDir+"/"+relativeDir);
			if (!targetDir.exists()) {
				FileUtils.forceMkdir(targetDir);
			}
			String realDir=rootDir+"/"+relativeDir+"/"+originFileName;
			String viewPath = relativeDir + File.separator + originFileName;

			String suffix = FileType.getSuffixByFilename(originFileName);
			if (!validType(suffix, (String[])conf.get("allowFiles"))) {
				return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
			}else {
				IOUtils.copy((InputStream) conf.get("file"), new FileOutputStream(realDir));
			}
			State storageState = new BaseState(true);
			if (storageState.isSuccess()) {
				storageState.putInfo("url", viewPath);
				storageState.putInfo("type", suffix);
				storageState.putInfo("original", realDir);
			}
			return storageState;
		} catch (Exception e) {
			return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
		}
	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);

		return list.contains(type);
	}
}
