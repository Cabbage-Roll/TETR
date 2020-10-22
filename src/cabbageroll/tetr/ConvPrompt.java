package cabbageroll.tetr;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class ConvPrompt extends StringPrompt{

    @Override
    public Prompt acceptInput(ConversationContext con, String answer) {
        // TODO Auto-generated method stub
        con.getForWhom().sendRawMessage("Answer: "+answer);
        return null;
    }

    @Override
    public String getPromptText(ConversationContext arg0) {
        // TODO Auto-generated method stub
        return "Width & height axis: (eg. x+ y-)";
    }

}
