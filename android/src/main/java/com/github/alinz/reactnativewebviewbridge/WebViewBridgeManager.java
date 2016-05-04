package com.github.alinz.reactnativewebviewbridge;

import javax.annotation.Nullable;
import java.util.Map;

import android.webkit.WebView;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.views.webview.ReactWebViewManager;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReactContext;

public class WebViewBridgeManager extends ReactWebViewManager {
  private static final String REACT_CLASS = "RCTWebViewBridge";

  public static final int COMMAND_EXEC_JS = 100;
  public static final int COMMAND_SEND_TO_BRIDGE = 101;

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @Override
  public @Nullable Map<String, Integer> getCommandsMap() {
    Map<String, Integer> commandsMap = super.getCommandsMap();

    commandsMap.put("exec", COMMAND_EXEC_JS);
    commandsMap.put("sendToBridge", COMMAND_SEND_TO_BRIDGE);

    return commandsMap;
  }

  @Override
  public void receiveCommand(WebView root, int commandId, @Nullable ReadableArray args) {
    super.receiveCommand(root, commandId, args);

    switch (commandId) {
      case COMMAND_EXEC_JS:
        execJS(root, args.getString(0));
        break;
      case COMMAND_SEND_TO_BRIDGE:
        sendToBridge(root, args.getString(0));
        break;
      default:
        //do nothing!!!!
    }
  }

  private void sendToBridge(WebView root, String message) {
    if (!message.isEmpty()) {
      String script = "WebViewBridge.onMessage('" + message + "');";
      root.evaluateJavascript(script, null);
    }
  }

  private void execJS(WebView root, String jsCode) {
    if (!jsCode.isEmpty()) {
      root.evaluateJavascript(jsCode, null);
    }
  }

  @Override
  protected WebView createViewInstance(ThemedReactContext reactContext) {
    WebView webView = super.createViewInstance(reactContext);
    webView.addJavascriptInterface(new JavascriptBridge((ReactContext)webView.getContext()), "WebViewBridgeAndroid");
    return webView;
  }
}
