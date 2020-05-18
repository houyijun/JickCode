import java.util.Date;

public class ${name} {


<#list children as param>
	public child{
    	key: ${param.name} value: ;
    }

</#list>
}