package cst235.servicepals;

import cst235.servicepals.controller.Controller;

/**
 * contains the main method
 */
public class ServicePals {
	public static void main(String[] args) {
		Controller.makeTestData();
		Controller.showMainMenu();
	}
}
