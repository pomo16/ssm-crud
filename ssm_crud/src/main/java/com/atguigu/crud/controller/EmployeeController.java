package com.atguigu.crud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.crud.bean.Employee;
import com.atguigu.crud.bean.Msg;
import com.atguigu.crud.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * ����Ա��CRUD����
 * @author pomo
 *
 */

@Controller
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;
	
	/**
	 * ������������һ
	 * ����ɾ����1-2-3
	 * ����ɾ����1
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/emp/{ids}",method=RequestMethod.DELETE)
	public Msg deleteEmp(@PathVariable("ids")String ids) {
		//����ɾ��
		if(ids.contains("-")) {
			List<Integer> del_ids = new ArrayList<>();
			String[] str_ids = ids.split("-");
			//��װid�ļ���
			for (String string : str_ids) {
				del_ids.add(Integer.parseInt(string));
			}
			employeeService.deleteBatch(del_ids);
		} else {
			Integer id = Integer.parseInt(ids);
			employeeService.deleteEmp(id);
		}
		return Msg.success();
	}
	
	/**
	 * ���ֱ�ӷ���ajax=PUT��ʽ������
	 * ��װ�����ݣ�
	 * Employee [empId=1013, empName=null, gender=null, email=null, dId=null]
	 * 
	 * ���⣺�������������ݣ�����Employee�����װ����
	 * update tbl_emp where emp_id = 1013
	 * 
	 * ԭ��
	 * Tomcat:
	 * 		1.���������е����ݣ���װһ��map
	 * 		2.request.getParameter("empName")�ͻ�����map��ȡֵ
	 * 		3.SpringMVC��װPOJO�����ʱ�򡣻��POJO��ÿ�����Ե�ֵ��request.getParameter("email");
	 * AJAX����PUT���������ĲҰ���
	 * 		PUT�����������е����ݣ�request.getParameter("empName")�ò���
	 * 		Tomcatһ����PUT�����װ�������е�����Ϊmap��ֻ��POST��ʽ������ŷ�װ������Ϊmap
	 * TomcatԴ��λ�ã�org.apache.catalina.connector.Request--parseParameters() (P3000+)
	 * 			if(!getConnector().isParseBodyMethod(getMethod())) {
	 * 				success = true;
	 * 				return;
	 * 			}
	 * 
	 * 			protected String parseBodyMethods = "POST";
	 * 
	 * ���������
	 * ����Ҫ��֧��ֱ�ӷ���PUT֮�������Ҫ��װ�������е�����
	 * 1.������FormContentFilter(springmvc5.1֮����иù�������֮ǰ�汾��ʹ��HttpPutFormContentFilter)
	 * 2.���������ǽ��������е����ݽ�����װ��һ��map
	 * 3.request�����°�װ��request.getParameter()����д���ͻ���Լ���װ��map��ȡ����
	 * Ա�����·���
	 * @param employee
	 * @return
	 */
	@RequestMapping(value="/emp/{empId}",method=RequestMethod.PUT)
	@ResponseBody
	public Msg saveEmp(Employee employee) {
		//System.out.println("��Ҫ���µ�Ա�����ݣ�" + employee);
		employeeService.updateEmp(employee);
		return Msg.success();
	}
	
	/**
	 * ����id��ѯԱ��
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/emp/{id}",method=RequestMethod.GET)
	@ResponseBody
	public Msg getEmp(@PathVariable("id") Integer id) {
		Employee employee = employeeService.getEmp(id);
		return Msg.success().add("emp",employee);
	}
	
	/**
	 * ����û����Ƿ����
	 * @param empName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkuser")
	public Msg checkuser(@RequestParam("empName") String empName) {
		//���ж��û����¶��ǺϷ��ı��ʽ;��˷�ʽ
		String regx = "(^[a-zA-Z0-9_-]{6,16}$)|([\\u2E80-\\u9FFF]{2,5})";
		if(!empName.matches(regx)) {
			return Msg.fail().add("va_msg","�û���ӦΪ6-16λӢ�ĺ����ֵ���ϻ���2-5λ����");
		}
		
		//���ݿ��û����ظ�У��
		boolean b = employeeService.checkUser(empName);
		if(b) {
			return Msg.success();
		} else {
			return Msg.fail().add("va_msg","�û���������");
		}
	}
	
	/**
	 * Ա������
	 * 1.֧��JSR303У��
	 * 2.����Hibernate-Validator
	 * @return
	 */
	@RequestMapping(value="/emp",method=RequestMethod.POST)
	@ResponseBody
	public Msg saveEmp(@Valid Employee employee,BindingResult result) {
		if(result.hasErrors()) {
			//У��ʧ��Ӧ�÷���ʧ�ܣ���ģ̬������ʾУ��ʧ�ܵĴ�����Ϣ
			Map<String, Object> map = new HashMap<>();
			List<FieldError> errors = result.getFieldErrors();
			for(FieldError fieldError : errors) {
				System.out.println("������ֶ�����" + fieldError.getField());
				System.out.println("������Ϣ��" + fieldError.getDefaultMessage());
				map.put(fieldError.getField(),fieldError.getDefaultMessage());
			}
			return Msg.fail().add("errorFields",map);
		} else {
			employeeService.saveEmp(employee);
			return Msg.success();
		}
		
	}
	
	/**
	 * ����Jackson��
	 * @param pn
	 * @return
	 */
	@RequestMapping("/emps")
	@ResponseBody
	public Msg getEmpsWithJson(@RequestParam(value = "pn", defaultValue = "1") Integer pn) {
		// �ⲻ��һ����ҳ��ѯ
		// ����PageHelper��ҳ���
		// �ڲ�ѯ֮ǰֻ��Ҫ���ã�����ҳ�룬�Լ�ÿҳ�Ĵ�С
		PageHelper.startPage(pn, 5);
		//�����������һ���������������emp_idΪ�������ݵ��ֶ�����ascΪ������ɣ�descΪ����ascΪ����
		PageHelper.orderBy("emp_id asc");
		// startPage��������������ѯ����һ����ҳ��ѯ
		List<Employee> emps = employeeService.getAll();
		// ʹ��pageInfo��װ��ѯ��Ľ����ֻ��Ҫ��pageInfo����ҳ������ˡ�
		// ��װ����ϸ�ķ�ҳ��Ϣ�����������ǲ�ѯ���������ݣ�����������ʾ��ҳ��
		PageInfo page = new PageInfo(emps, 5);
		return Msg.success().add("pageInfo",page);
	}

	/**
	 * ��ѯԱ�����ݣ���ҳ��ѯ��
	 * @return
	 */
	//@RequestMapping("/emps")
	public String getEmps(@RequestParam(value="pn",defaultValue="1")Integer pn,Model model) {
		//�ⲻ��һ����ҳ��ѯ
		//����PageHelper��ҳ���
		//�ڲ�ѯ֮ǰֻ��Ҫ���ã�����ҳ�룬�Լ�ÿҳ�Ĵ�С
		PageHelper.startPage(pn, 5);
		//startPage��������������ѯ����һ����ҳ��ѯ
		List<Employee> emps = employeeService.getAll();
		//ʹ��pageInfo��װ��ѯ��Ľ����ֻ��Ҫ��pageInfo����ҳ������ˡ�
		//��װ����ϸ�ķ�ҳ��Ϣ�����������ǲ�ѯ���������ݣ�����������ʾ��ҳ��
		PageInfo page = new PageInfo(emps,5);
		model.addAttribute("pageInfo", page);
		
		return "list";
	}
}
