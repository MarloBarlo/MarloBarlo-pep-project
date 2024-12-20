package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    MessageService messageService;
    AccountService accountService;
    private final ObjectMapper objectMapper;

    public SocialMediaController(){
    this.messageService = new MessageService();
    this.accountService = new AccountService();
    this.objectMapper = new ObjectMapper();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        initializeRoutes(app);
        return app; // Return the Javalin app object
    }

    private void initializeRoutes(Javalin app) {
        // Registration Endpoint
        app.post("/register", this::registerUser );
        
        // Login Endpoint
        app.post("/login", this::loginUser );
        
        // Create Message Endpoint
        app.post("/messages", this::createMessage);
        
        // Get All Messages Endpoint
        app.get("/messages", this::getAllMessages);
        
        // Get Message by ID Endpoint
        app.get("/messages/{message_id}", this::getMessageById);
        
        // Delete Message Endpoint
        app.delete("/messages/{message_id}", this::deleteMessage);
        
        // Update Message Endpoint
        app.patch("/messages/{message_id}", this::updateMessage);
        
        // Get Messages by User ID
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserId);
    }


    private void registerUser (Context ctx) {
        try {
            Account account = objectMapper.readValue(ctx.body(), Account.class);
            accountService.register(account);
            Account createdAccount = accountService.getAccountByUsername(account.getUsername());
            ctx.status(200).json(createdAccount); // Return created status
        } catch (IllegalArgumentException | JsonProcessingException e) {
            ctx.status(400).body().toString();
        }
    }

    private void loginUser (Context ctx) {
        try {
            Account account = objectMapper.readValue(ctx.body(), Account.class);
            Account loggedInAccount = accountService.login(account.getUsername(), account.getPassword());
            ctx.status(200).json(loggedInAccount);
        } catch (IllegalArgumentException | JsonProcessingException e) {
            ctx.status(401).body().toString();
        }
    }

    private void createMessage(Context ctx) {
        try {
            Message message = objectMapper.readValue(ctx.body(), Message.class);
            messageService.createMessage(message);
            Message createdMessage = messageService.getMessageByText(message.getMessage_text());
            ctx.status(200).json(createdMessage); // Return created status
        } catch (IllegalArgumentException | JsonProcessingException e) {
            ctx.status(400).body().toString();
        }
    }

    private void getAllMessages(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.status(200).json(messages); // Return all messages
    }

    private void getMessageById(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.getMessageById(messageId);
            if (message != null) {
                ctx.status(200).json(message); // Return the specific message
            } else {
                ctx.status(200).body().toString();
            }
        } catch (NumberFormatException e) {
            ctx.status(400);
        }
    }

    private void deleteMessage(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.getMessageById(messageId);
            boolean deleted = messageService.deleteMessage(messageId);
            if (deleted == true) {
                ctx.status(200).json(message); // show deleted message
            } else if (deleted = false){
                ctx.status(200).body(); // no message to delete
            }
        } catch (NumberFormatException e) {
            ctx.status(400);
        }
    }

    private void updateMessage(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message updatedMessage = objectMapper.readValue(ctx.body(), Message.class);
            updatedMessage.setMessage_id(messageId); // Ensure the ID is set for the update
            messageService.updateMessage(updatedMessage);
            Message message = messageService.getMessageById(messageId);
            ctx.status(200).json(message); // Return the updated message
        } catch (IllegalArgumentException | JsonProcessingException e) {
            ctx.status(400).body().toString();
        }
    }

    private void getMessagesByUserId(Context ctx) {
        try {
            int accountId = Integer.parseInt(ctx.pathParam("account_id"));
            List<Message> messages = messageService.getMessagesByUserId(accountId);
            ctx.status(200).json(messages); // Return messages for the specific user
        } catch (NumberFormatException e) {
            ctx.status(400).body().toString();
        }
    }



}