package com.aa.act.interview.org;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Organization {

	private final Position root;
	
	public Organization() {
		root = createOrganization();
	}
	
	protected abstract Position createOrganization();
	
	/**
	 * hire the given person as an employee in the position that has that title
	 * 
	 * @param person Name object of the employee
	 * @param title Title in OrgChart to search for
	 * @return the newly filled position or empty if no position has that title
	 */
	public Optional<Position> hire(Name person, String title) {

		// Breadth first search

		// Create queue. Use a LinkedList but only the queue interface
		Queue<Position> searchQueue = new LinkedList<>();

		int identifier = ThreadLocalRandom.current().nextInt(0, 1000000001);

		// Assume the root is always initialized properly.
		searchQueue.add(root);

		// Run through the entire org chart
		while(!searchQueue.isEmpty()){

			// Grab the next position to check
			Position current = searchQueue.remove();

			// Is this the correct position?
			if(current.getTitle().equals(title)){

				// Is the position already filled?
				if(current.getEmployee().isPresent()){
					// this is a discussion point. What do we do when there is already an employee?
					// ~Ignore?
					// ~Exception?
					// ~Replace?
					// ~Should we have multiple employees in a position? (multiple salesmen).
					// 		If so, we need to distinguish between single-employee positions and multi-employee positions
					//
					//
					// Currently, I choose to ignore.
				}
				else {
					// Create a new employee and add it to this position
					current.setEmployee(Optional.of(new Employee(identifier, person)));
					return Optional.of(current);
				}
			}

			// add the direct reports to the search queue to extend the search frontier.
			searchQueue.addAll(current.getDirectReports());
		}

		// If the position is not found, return empty.
		// We could throw an exception then have the caller catch it to handle this case.
		return Optional.empty();
	}

	@Override
	public String toString() {
		return printOrganization(root, "");
	}
	
	private String printOrganization(Position pos, String prefix) {
		StringBuffer sb = new StringBuffer(prefix + "+-" + pos.toString() + "\n");
		for(Position p : pos.getDirectReports()) {
			sb.append(printOrganization(p, prefix + "\t"));
		}
		return sb.toString();
	}
}
