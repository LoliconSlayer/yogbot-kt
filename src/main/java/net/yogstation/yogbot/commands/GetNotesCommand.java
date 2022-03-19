package net.yogstation.yogbot.commands;

import net.yogstation.yogbot.Yogbot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class GetNotesCommand extends PermissionsCommand {

	protected List<String> getNotes(String ckey, boolean showAdmin) {
		try (Connection connection = Yogbot.database.getConnection();
			 PreparedStatement notesStmt = connection.prepareStatement(String.format(
				 "SELECT timestamp, text, adminckey FROM `%s` WHERE `targetckey` = ? AND `type`= \"note\" AND deleted = 0 AND (expire_timestamp > NOW() OR expire_timestamp IS NULL) AND `secret` = 0 ORDER BY `timestamp`",
				 Yogbot.database.prefix("messages")
			 ))
		) {
			notesStmt.setString(1, ckey);
			ResultSet notesResult = notesStmt.executeQuery();

			final List<String> messages = new ArrayList<>();
			StringBuilder notesString = new StringBuilder("Notes for ").append(ckey).append("\n");
			while (notesResult.next()) {
				String nextNote = String.format(
					"```%s\t%s%s```",
					notesResult.getDate("timestamp").toString(),
					notesResult.getString("text"),
					showAdmin ? String.format("   %s", notesResult.getString("adminckey")) : ""
				);
				if (notesString.length() + nextNote.length() > 2000) {
					messages.add(notesString.toString());
					notesString.setLength(0);
				}
				notesString.append(nextNote);
			}
			messages.add(notesString.toString());
			return messages;
		} catch (SQLException e) {
			LOGGER.error("Error getting notes", e);
			return List.of("A SQL Error has occurred");
		}
	}
}
