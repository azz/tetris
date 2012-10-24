package net;

/**
 * Tetris Transfer Protocol :P
 *
 * @author derflatulator
 */
public class TTP {

	public String processInput(String input) {
		if (input.length() > 0) {
			if (input.charAt(0) == 'S') {
				// Sync acknowledged
				return "ACK";
			} else if (input.charAt(0) == 'P'){
				return processPacket(input.substring(1));
			}
		}
		
		// UNKnown message
		return "UNK";
	}

	private String processPacket(String input) {

		try {
			if (input.charAt(0) == 'G') {
				// game
				String gameData = input.split("\n")[1];
				
			} else if (input.charAt(0) == 'P') {
				// position
				
			}
			
			return "ERR";
		} catch (Exception ex) {
			return "ERR";
		}
	}
}
