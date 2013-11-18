/* 
 * Copyright 2013 Muthukumaran (https://github.com/muthuishere/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.multimachine.views.console;

import com.multimachine.utils.StringHelper;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import javax.swing.text.*;
import org.apache.log4j.Logger;

/**
 *
 * @author hutchuk
 */
public class ColoredCommandFilter extends DocumentFilter {

    private ColoredSwingConsoleWindow parent;

    private static final Logger log = Logger.getLogger(ColoredCommandFilter.class);

    public boolean handleApplicationCommand(String cmd) {

        for (Method method : parent.getClass().getMethods()) {
            // checks if there is annotation present of the given type Developer
            if (method.isAnnotationPresent(ConsoleCommand.class)) {
                try {
                    // iterates all the annotations available in the method
                    for (Annotation anno : method.getDeclaredAnnotations()) {
                        System.out.println("Annotation in Method '" + method + "' : " + anno);
                        ConsoleCommand a = method.getAnnotation(ConsoleCommand.class);
                        if (cmd.equals(a.value())) {
                            System.out.println(cmd + " has a handler" + method);
                            method.invoke(parent, null);
                            return true;

                        }
                    }
                } catch (Throwable ex) {
                    log.error("Error while invoking handler", ex);
                }
            }
        }
        return false;

    }

    public ColoredCommandFilter(ColoredSwingConsoleWindow parent) {
        this.parent = parent;

    }

    @Override

    public void insertString(DocumentFilter.FilterBypass fb, int offset, String string,
            AttributeSet attr) throws BadLocationException {
        if (string == null) {
            return;
        } else {
            replace(fb, offset, 0, string, attr);
        }
    }

    @Override
    public void remove(DocumentFilter.FilterBypass fb, int offset,
            int length) throws BadLocationException {
        replace(fb, offset, length, "", null);
    }

    @Override
    public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
            String text, AttributeSet attrs) throws BadLocationException {
        Document doc = fb.getDocument();
        Element root = doc.getDefaultRootElement();
        int count = root.getElementCount();
        int index = root.getElementIndex(offset);
        Element cur = root.getElement(index);
        int promptPosition = cur.getStartOffset() + parent.prompt.length();

        /*
         log.info(text.equals(StringHelper.NEW_LINE) +" is new line , Inside replace" + text);
         log.info(text.equals("\n") +" is new line , Inside replace" + text);
        
         log.info(text.length() + "text length Ascii value " +((int) text.charAt(0)));
         log.info(text.length() + "text length new line " +((int) '\n'));
         log.info((index == count - 1) +"index == count - 1" );
         log.info((offset) +"offset" );
         log.info((promptPosition) +"promptPosition" );
         */
        if (index == count - 1 && offset - promptPosition >= 0) {
            if (text.equals("\n")) {
                String cmd = doc.getText(promptPosition, offset - promptPosition);
                if (cmd.isEmpty()) {
                    //text = "\n"+ parent.s;
                    parent.showPrompt(true);
                    return;
                } else {
                    log.info("Command object fetched " + cmd);
                    if (!handleApplicationCommand(cmd)) {
                        parent.sendCommand(cmd);
                    }

                    //return;
                    //text = "\n" + + "\n" + prompt;
                }
            }
            fb.replace(offset, length, text, attrs);
        }
    }
}
