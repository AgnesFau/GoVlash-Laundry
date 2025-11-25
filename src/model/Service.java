package model;

import java.util.ArrayList;

public class Service {
	private int id;
	private String name;
	private String description;
	private Double price;
	private int duration;
	
	private static ArrayList<Service> listService = new ArrayList<Service>();

	public Service(String name, String description, Double price, int duration) {
		super();
		this.id = listService.size()+1;
		this.name = name;
		this.description = description;
		this.price = price;
		this.duration = duration;
		listService.add(this);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public static ArrayList<Service> getListService() {
		return listService;
	}

	public static void setListService(ArrayList<Service> listService) {
		Service.listService = listService;
	}
}
