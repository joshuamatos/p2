package edu.uprm.cse.datastructures.cardealer.model;

import java.util.Comparator;

public class CarComparator implements Comparator<Car>{

	@Override
	public int compare(Car car1, Car car2) {
		String currentCar = car1.getCarBrand() + car1.getCarModel() + car1.getCarModelOption();
		String otherCar = car2.getCarBrand() + car2.getCarModel() + car2.getCarModelOption();
		return currentCar.compareTo(otherCar);
	}
	
}
