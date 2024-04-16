import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

import java.util.List;

public class Extension implements BurpExtension
{
    @Override
    public void initialize(MontoyaApi montoyaApi)
    {
        montoyaApi.extension().setName("Response Hider");

        List<String> headersToHide = List.of(
                "Cookie",
                "Vary",
                "Content-Type"
        );

        montoyaApi.userInterface().registerHttpResponseEditorProvider(editorCreationContext ->
                new MyExtensionProvidedHttpResponseEditor(montoyaApi.userInterface(), editorCreationContext, headersToHide)
        );
    }
}
