package com.employeeCrudapp.Controller;

import java.util.List;

import javax.management.InvalidAttributeValueException;
import javax.validation.Valid;
import org.springframework.web.bind.*;
import com.employeeCrudapp.Exception.InvalidValueException;
import com.employeeCrudapp.entity.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.employeeCrudapp.entity.Employee;
import com.employeeCrudapp.service.BranchService;
import com.employeeCrudapp.service.EmployeeService;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private BranchService branchService;

    @Autowired
    public EmployeeRestController(EmployeeService theEmployeeService) {
        employeeService = theEmployeeService;
    }
    
 

    // expose "/employees" and return list of employees
    @GetMapping("/employees")
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    // add mapping for GET /employees/{employeeId}

    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable int employeeId) {

        Employee theEmployee = employeeService.findById(employeeId);

        if (theEmployee == null) {
            throw new InvalidValueException("Employee id not found - " + employeeId);
        }

        return theEmployee;
    }

    // add mapping for POST /employees - add new employee

    @PostMapping("/employees")
    public Employee addEmployee(@RequestParam Integer branchId, @Valid @RequestBody Employee theEmployee) {

      // validate age from National ID
    	employeeService.validateAgeFromNationalId(theEmployee.getNationalId(),theEmployee.getAge());

        Branch branch = branchService.findBranchById(branchId);
        
        if (branch == null) {
            throw new InvalidValueException("The Branch with ID: "+branchId+" is not found " );
        }
        
        theEmployee.setBranch(branch);
        employeeService.save(theEmployee);

        return theEmployee;
    }

    // add mapping for PUT /employees - update existing employee

    @PutMapping("/employees")
    public Employee updateEmployee(@Valid @RequestBody Employee theEmployee) {
    	
    	int branchId = theEmployee.getBranch().getId();
        Employee tempEmployee = employeeService.findById(theEmployee.getId());
        if (tempEmployee == null) {
            throw new InvalidValueException("Employee id not found - " + theEmployee.getId());
        }

    	// validate age from National ID
     employeeService.validateAgeFromNationalId(theEmployee.getNationalId(),theEmployee.getAge());
     Branch branch = branchService.findBranchById(theEmployee.getBranch().getId());
     
     if (branch == null) {
         throw new InvalidValueException("The Branch with ID: "+branchId+" is not found " );
     }

        employeeService.update(theEmployee);

        return theEmployee;
    }

    // add mapping for DELETE /employees/{employeeId} - delete employee

    @DeleteMapping("/employees/{employeeId}")
    public String deleteEmployee(@PathVariable int employeeId) {
    	

        Employee tempEmployee = employeeService.findById(employeeId);
        
        

		if (tempEmployee == null) {
            throw new InvalidValueException("Employee id not found - " + employeeId);
        }
        employeeService.deleteById(employeeId);

        return "Deleted employee id - " + employeeId;
    }
    
    //Exception Handling

    @ExceptionHandler
    public String handleInvalidValueException(InvalidValueException exception) {
    	
		return exception.getMessage();
    	
    	
    }
     
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public String exception(MethodArgumentNotValidException exception) {
    	
    	if(exception.getMessage().contains("^[ء-ي ]")) {
    		return "name must be in arabic !!!";
    	}else 
    	 if(exception.getMessage().contains("size must be between 14 and 14"))
    	{
    		return "NationalId length must be 14 !!!";
    	}
    	
		return exception.getMessage();

    	}
    		
    	
		
    


}










