package com.example.assembly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.assembly.response.Response;
import com.example.assembly.service.JavaAssemblyService;

@RestController
@RequestMapping()
public class JavaAssemblyConroller {
	
	@Autowired
	JavaAssemblyService javaAssemblyService;
	
	@PostMapping("/addition")
	public ResponseEntity<Response> add(@RequestBody MultipartFile statements){
		Integer result = null;
		Response response = new Response();
		if(statements == null) {
			response.setStatus("A file is required containing operations");
			response.setResult(result);
			return new ResponseEntity<Response>(response,HttpStatus.BAD_REQUEST);
		}
		try {
			result = javaAssemblyService.addition(statements);
			response.setStatus("successful additon");
			response.setResult(result);
			return new ResponseEntity<Response>(response,HttpStatus.OK);
		} catch(RuntimeException exception) {
			response.setStatus("RuntimeException encountered while performing operations. " + exception.getMessage());
			response.setResult(result);
			return new ResponseEntity<Response>(response,HttpStatus.BAD_REQUEST);
		} catch(Exception exception) {
			response.setStatus("Exception encountered while performing operations. " + exception.getMessage());
			response.setResult(result);
			return new ResponseEntity<Response>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
