package test.rackSpace;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.bind.Binder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/*
 * Class that is used for marshalling and un marshalling the tasks
 */
public class JAXBParser {

	JAXBContext jaxbContext;
	Marshaller marshaller;

	/**
	 * Initializes the jaxb context
	 * @throws JAXBException
	 */
	private void JAXBInitializer() throws JAXBException {

		jaxbContext = JAXBContext.newInstance(Tasks.class);
		marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	}

	/**
	 * Write new Task into the XML file on POST
	 * 
	 * @param task
	 * @throws IOException
	 * @throws JAXBException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public void writeToXML(Task task) throws IOException, JAXBException,
			ParserConfigurationException, SAXException {

		JAXBInitializer();
		Tasks taskList = new Tasks();

		taskList.setTasks(new ArrayList<Task>());

		File xml = new File("File.xml");
		if (xml.exists() && xml.length() != 0) {

			Binder<Node> binder = jaxbContext.createBinder();
			Tasks unmarshalled = (Tasks) binder.unmarshal(parseXmlFile(xml));

			boolean flag = false;
			List<Task> existingTasks = unmarshalled.getTasks();
			
			if (unmarshalled != null && !existingTasks.isEmpty()) {

				for (Task taskObject : existingTasks) {

					if (taskObject.getTaskTitle().equals(task.getTaskTitle())) {
						taskObject.setTaskStatus(task.getTaskStatus());
						flag = true;
						break;
					}
				}
				if (!flag) {
					existingTasks.add(task);
				}
			}
			taskList.setTasks(existingTasks);
		} else {
			taskList.getTasks().add(task);
		}
		marshaller.marshal(taskList, new FileOutputStream("File.xml"));
	}

	/**
	 * 
	 * @param xml
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private Document parseXmlFile(File xml)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(xml);
		return document;
	}

	/**
	 * Retrieves the tasks from the XML file
	 * 
	 * @return
	 * @throws JAXBException
	 * @throws ParseException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public Tasks getTasksFromXML() throws JAXBException, ParseException,
			IOException, ParserConfigurationException, SAXException {

		JAXBInitializer();

		Tasks tasks = new Tasks();

		File xml = new File("File.xml");
		if (xml.exists() && xml.length() != 0) {

			Binder<Node> binder = jaxbContext.createBinder();
			tasks = (Tasks) binder.unmarshal(parseXmlFile(xml));

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

			String dateString = dateFormat.format(Calendar.getInstance().getTime());
			Date date = dateFormat.parse(dateString);

			if (tasks != null) {
				for (Task task : tasks.getTasks()) {
					Date xmlDate = dateFormat.parse(task.getDueDate());
					TaskStatus taskStatus = task.getTaskStatus();
					if (date.compareTo(xmlDate) > 0) {
						if (!taskStatus.equals(TaskStatus.COMPLETED)) {
							task.setTaskStatus(TaskStatus.TASK_DUE);
						}
					} else if (date.compareTo(xmlDate) < 0) {
						if (!taskStatus.equals(TaskStatus.COMPLETED)) {
							task.setTaskStatus(TaskStatus.PENDING);
						}
					}
				}
			}
		}
		return tasks;
	}
}