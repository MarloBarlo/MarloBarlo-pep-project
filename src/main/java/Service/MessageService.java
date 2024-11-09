package Service;

import Model.Message;
import DAO.MessageDAO;
import DAO.AccountDAO;

import java.util.List;

public class MessageService {
    public MessageDAO messageDAO;
    public AccountDAO accountDAO;

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
                throw new IllegalArgumentException("message text error");
        }

        if (messageDAO.getAllMessagesFromUser(message.getPosted_by()).equals(null) || messageDAO.getAllMessagesFromUser(message.getPosted_by()).isEmpty()) {
            throw new IllegalArgumentException("message text error");
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

    public Message getMessageByText(String text) {
        return messageDAO.getMessageByText(text); //Retrive message
    }

    public boolean deleteMessage(int id) {
        Message existingMessage = messageDAO.getMessageByMessageId(id);
        if (existingMessage != null){
            messageDAO.deleteMessageByMessageId(id); //Deletes message
            return true;
        } 
        return false;
    }

    public Boolean updateMessage(Message updatedMessage) {
        // Check if the message exists
        Message existingMessage = messageDAO.getMessageByMessageId(updatedMessage.getMessage_id());
        if (existingMessage == null || updatedMessage.getMessage_text().length() > 255 ||
            updatedMessage.getMessage_text().isEmpty() || updatedMessage.getMessage_text() == null) {
                // 
                throw new IllegalArgumentException("message not found or message text error");
        }
        // Update the existing message with new values
        messageDAO.updateMessage(updatedMessage.getMessage_id(), updatedMessage); // Call the DAO to update the message
        return true; // Return the updated message
    }

    public List<Message> getMessagesByUserId(int user) {
        return messageDAO.getAllMessagesFromUser(user); 
    }



}
