import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.responses.HttpResponse;
import burp.api.montoya.ui.Selection;
import burp.api.montoya.ui.UserInterface;
import burp.api.montoya.ui.editor.EditorOptions;
import burp.api.montoya.ui.editor.RawEditor;
import burp.api.montoya.ui.editor.extension.EditorCreationContext;
import burp.api.montoya.ui.editor.extension.EditorMode;
import burp.api.montoya.ui.editor.extension.ExtensionProvidedHttpResponseEditor;

import java.awt.*;
import java.util.List;

import static burp.api.montoya.http.message.HttpRequestResponse.httpRequestResponse;

public class MyExtensionProvidedHttpResponseEditor implements ExtensionProvidedHttpResponseEditor
{
    private final RawEditor editor;
    private final List<String> headersToHide;

    private HttpRequestResponse requestResponse;

    public MyExtensionProvidedHttpResponseEditor(UserInterface userInterface, EditorCreationContext creationContext, List<String> headersToHide)
    {
        this.headersToHide = headersToHide;
        if (creationContext.editorMode() == EditorMode.READ_ONLY)
        {
            editor = userInterface.createRawEditor(EditorOptions.READ_ONLY);
        }
        else {
            editor = userInterface.createRawEditor();
        }
    }

    @Override
    public HttpResponse getResponse()
    {
        return requestResponse.response();
    }

    @Override
    public void setRequestResponse(HttpRequestResponse httpRequestResponse)
    {
        HttpResponse response = httpRequestResponse.response();

        for (String header : headersToHide)
        {
            response = response.withRemovedHeader(header);
        }

        requestResponse = httpRequestResponse(httpRequestResponse.request(), response);

        editor.setContents(response.toByteArray());
    }

    @Override
    public boolean isEnabledFor(HttpRequestResponse httpRequestResponse)
    {
        return true;
    }

    @Override
    public String caption()
    {
        return "Hidden headers";
    }

    @Override
    public Component uiComponent()
    {
        return editor.uiComponent();
    }

    @Override
    public Selection selectedData()
    {
        return editor.selection().isPresent() ? editor.selection().get() : null;
    }

    @Override
    public boolean isModified()
    {
        return editor.isModified();
    }
}
