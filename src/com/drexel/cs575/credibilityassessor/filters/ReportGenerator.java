/**
 * Mike Kozak (Student ID 10223568)
 * CS575 Assignment 3
 * 
 */
package com.drexel.cs575.credibilityassessor.filters;

import com.drexel.cs575.credibilityassessor.datamodel.TextMessage;
import com.drexel.cs575.credibilityassessor.util.TextTokenizer;

/**
 * This class is meant to act as a pipe/filter mechanism. In a more complete system, a config file would assign workflows to data
 * types which this class would use to drive data from filter to filter until eventually reaching the end of the workflow. For testing
 * purposes, this class instead wires utility functions together based on request made. A full credibility report would include more than
 * Type-Token-Ratio, but would require machine learning to tune a classifier based on numerous inputs and that is outside the scope of a 
 * class project.
 * 
 * @author Computer
 *
 */
public class ReportGenerator {
	
	/**
	 * This takes a TTR request and produces a TTR report to be displayed by the UI
	 * @return
	 */
	public String generateTTRReport(TextMessage message) {
		String report = "";
		
		if(message != null) {
			report = "TTR ";
			if(message.getId() >= 0) {
				report += "for message ID:" + message.getId() + " ";
			}
			if(!message.getContact().equals("")) {
				report += "from Contact:" + message.getContact() + " ";
			}
			
			if(!message.getText().equals("")) {
				double ratio = TextTokenizer.deriveTypeTokenRatio(message.getText());
				report += "= " + ratio;
			} else {
				report += "= 0. No message(s) found.";
			}
		}
		
		return report;
	}
}
