package test.rackSpace;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
/*
 * Class implements Get and Post requests for restful web service
 */
@Path("/todo")
public class ToDo {
/**
 * Returns the list of tasks
 * @return
 * @throws Exception
 */
	@GET
	@Produces("application/xml")
	public Tasks getToDo() throws Exception {

		JAXBParser jax = new JAXBParser();
		return jax.getTasksFromXML();
	}
/**
 * Creates the task and adds to the XML file
 * @param taskTitle
 * @param taskStatus
 * @param dueDate
 * @return
 * @throws JAXBException
 * @throws IOException
 * @throws ParserConfigurationException
 * @throws SAXException
 */
	@POST
	@Path("/{param1}/{param2}/{param3}")
	public Response postToDo(@PathParam("param1") String taskTitle,
			@PathParam("param2") String taskStatus,
			@PathParam("param3") String dueDate) throws JAXBException,
			IOException, ParserConfigurationException, SAXException {

		JAXBParser jax = new JAXBParser();
		Task task = new Task();
		task.setTaskTitle(taskTitle);
		task.setDueDate(dueDate);
		if ((taskStatus.equalsIgnoreCase(TaskStatus.COMPLETED.toString()))) {
			task.setTaskStatus(TaskStatus.COMPLETED);
		} else
			task.setTaskStatus(TaskStatus.PENDING);

		jax.writeToXML(task);

		return Response.ok().build();
	}

}