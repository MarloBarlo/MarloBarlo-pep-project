package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    public MessageDAO messageDAO;

    //No arg constructor
    public MessageService(){
        messageDAO = new MessageDAO();
    }

    //arg construtor
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    
    public Message createMessage(Message message) {
        // Validate message text and postedBy
        if (message.getMessage_text() == null || message.getMessage_text().isEmpty() ||
            message.getMessage_text().length() > 255){
            messageDAO.createMessage(message);
            return null;
        }
        //Save new message
        messageDAO.createMessage(message);
        return message; //return the message
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages(); //Retrive all messages
    }

    public Message getMessageById(int id) {
        return messageDAO.getMessageByMessageId(id); //Retrive message
    }

    public boolean deleteMessage(int id) {
        messageDAO.deleteMessageByMessageId(id); //Delets message
        return true;
    }

    public Message updateMessage(Message updatedMessage) {
        // Check if the message exists
        Message existingMessage = messageDAO.getMessageByMessageId(updatedMessage.getMessage_id());
        if (existingMessage != null) {
            // Update the existing message with new values
            messageDAO.updateMessage(updatedMessage.getMessage_id(), updatedMessage); // Call the DAO to update the message
            return existingMessage; // Return the updated message
        }
        return null;
    }

    public List<Message> getMessagesByUserId(int user) {
        return messageDAO.getAllMessagesFromUser(user); // Assuming this method is implemented in MessageDAO
    }



}
