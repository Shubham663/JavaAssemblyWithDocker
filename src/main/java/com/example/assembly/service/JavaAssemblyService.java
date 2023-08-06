package com.example.assembly.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.assembly.entity.AdditionStatus;
import com.example.assembly.repository.JavaAssemblyAdditionRepository;

@Service
public class JavaAssemblyService {
	
	@Autowired
	JavaAssemblyAdditionRepository javaAssemblyAdditionRepository;
	
	public Integer addition(MultipartFile file) throws Exception{

		Integer result=0,addOpNo=0;
		Map<String,Integer> registers = new HashMap<>();
		InputStream inputStream = file.getInputStream();
		List<String> lines = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
		                    .lines().collect(Collectors.toList());
		for(String line : lines){
			String originalStatement = line;
			line=line.strip();
			if(line.startsWith("MV")) {
				line=line.substring(2);
				List<String> tokens = new ArrayList<>(Arrays.asList(line.split(",")));
				if(tokens.size()!=2) {
					writeToDB(0,"Invalid Statement " + originalStatement,result);
					throw new RuntimeException("Invalid Statement " + originalStatement);
				}
				Integer val = registers.get(tokens.get(0).strip());
				if(val == null)
					val=Integer.valueOf(0);
				registers.put(tokens.get(0).strip(), val + Integer.parseInt(tokens.get(1).strip().substring(1)));
			}
			else if(line.startsWith("ADD")){
				addOpNo++;
				if(addOpNo > 1) {
					writeToDB(0,"Tried to perform more than one add operation",result);
					throw new RuntimeException("Tried to perform more than one add operation");
				}
				line=line.substring(3);
				List<String> tokens = new ArrayList<>(Arrays.asList(line.split(",")));
				if(tokens.size()!=2) {
					writeToDB(0,"Invalid Statement " + originalStatement,result);
					throw new RuntimeException("Invalid Statement " + originalStatement);
				}
				Integer val = registers.get(tokens.get(0).strip());
				String secondToken  = tokens.get(1).strip();
				if(val == null)
					val=Integer.valueOf(0);
				if(Character.isLetter(secondToken.charAt(0))) {
					if(registers.get(secondToken)!=null)
						result = val+ registers.get(secondToken);
					else {
						writeToDB(0,"The register " + secondToken + " does not contain any value",result);
						throw new RuntimeException("The register " + secondToken + " does not contain any value");
					}
				}
				else
					result = val + Integer.parseInt(secondToken);
				registers.put(tokens.get(0).stripTrailing(), result);
			}
			else if(line.equals("SHOW REG")) {
				writeToDB(1,"Addition successful",result);
				return result;
			}
			else {
				writeToDB(0,"Statement was not recognised" + originalStatement,result);
				throw new RuntimeException("Statement was not recognised" + originalStatement);
			}
		}
		writeToDB(0,"'SHOW REG' statement was not given even once",result);
		throw new RuntimeException("'SHOW REG' statement was not given even once");
	}

	private void writeToDB(int successful, String comment, Integer result) throws Exception {
		AdditionStatus additionStatus = new AdditionStatus();
		String status = successful == 1 ? "Success" : "Failure";
		additionStatus.setStatus(status);
		additionStatus.setComment(comment);
		additionStatus.setProgramId(String.valueOf(ProcessHandle.current().pid()));
		additionStatus.setResult(result);
		try {
			javaAssemblyAdditionRepository.save(additionStatus);
		}
		catch(Exception exception) {
			throw new Exception("Exception encountered while writing to database. successful: " + successful + ", comment: " + comment + ", result: " + result);
		}
	}
}
