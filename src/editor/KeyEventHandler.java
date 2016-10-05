package editor;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.Node;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
/**
 * Created by Yao on 2016/10/1.
 */

public class KeyEventHandler implements EventHandler<KeyEvent> {
    int textCenterX;
    int textCenterY;

    private static final int STARTING_TEXT_POSITION_X = 250;
    private static final int STARTING_TEXT_POSITION_Y = 250;
    private static final int STARTING_FONT_SIZE = 20;

    /**
     * The Text to display on the screen.
     */
    private Text displayText = new Text(STARTING_TEXT_POSITION_X, STARTING_TEXT_POSITION_Y, "");
    private int fontSize = STARTING_FONT_SIZE;

    private String fontName = "Verdana";

    //10.2
    public Editor editor;
    Group root;
    static int lineLength = 450;
    int charHeight = 0;
    KeyEventHandler(final Group root, int windowWidth, int windowHeight, Editor editor) {
        this.root = root;
        this.editor = editor;
        textCenterX = 15;
        textCenterY = 100;

        // Initialize some empty text and add it to root so that it will be displayed.
        //displayText = new Text(textCenterX, textCenterY, "");
        // Always set the text origin to be VPos.TOP! Setting the origin to be VPos.TOP means
        // that when the text is assigned a y-position, that position corresponds to the
        // highest position across all letters (for example, the top of a letter like "I", as
        // opposed to the top of a letter like "e"), which makes calculating positions much
        // simpler!
    }
    public void removeCurrent(int code){
        //code == 1 add remove from the next of current node
        //code == 2 delete remove from current node
        int end = root.getChildren().size();
        FastLinkedListNode removetemp = new FastLinkedListNode();
        if(code == 1)
            removetemp = editor.buffer.getCurrentNode().getNext();
        if(code == 2)
            removetemp = editor.buffer.getCurrentNode();

        while (removetemp != null) {
            Text displayText = removetemp.getItem();
            System.out.println("remove"+displayText.getText());
            root.getChildren().remove(displayText);
            int charWidth = (int) Math.round(displayText.getLayoutBounds().getWidth());
            charHeight = (int) Math.round(displayText.getLayoutBounds().getHeight());
            editor.textLength -= charWidth;

            editor.positionX = (editor.textLength) % lineLength;
            editor.positionY = charHeight * ((editor.textLength) / lineLength) + 100;

            editor.cur.setx((editor.curLength) % lineLength);
            editor.cur.sety(charHeight * ((editor.curLength) / lineLength) + 100 - charHeight * 2 / 3);
            removetemp = removetemp.getNext();
        }
    }
    @Override
    public void handle(KeyEvent keyEvent) {
        if (keyEvent.getEventType() == KeyEvent.KEY_TYPED) {
            // Use the KEY_TYPED event rather than KEY_PRESSED for letter keys, because with
            // the KEY_TYPED event, javafx handles the "Shift" key and associated
            // capitalization.
            String characterTyped = keyEvent.getCharacter();
            if (characterTyped.length() > 0 && characterTyped.charAt(0) != 8) {
                // Ignore control keys, which have zero length, as well as the backspace
                // key, which is represented as a character of value = 8 on Windows.
                //displayText.setText(characterTyped);
                removeCurrent(1);
                editor.add(characterTyped);
                FastLinkedListNode temp = editor.buffer.getCurrentNode();
                int charWidthAdd = (int) Math.round(temp.getItem().getLayoutBounds().getWidth());
                //editor.textLength += charWidthAdd;
                editor.curLength += charWidthAdd;

                while (temp != null) {
                    Text displayText = temp.getItem();
                    System.out.println("add"+ displayText.getText());
                    displayText.setFont(Font.font(fontName, fontSize));

                    displayText.setX(editor.positionX);
                    displayText.setY(editor.positionY);

                    temp.updateXPos(editor.positionX);
                    temp.updateYPos(editor.positionY);

                    root.getChildren().add(displayText);

                    int charWidth = (int) Math.round(temp.getItem().getLayoutBounds().getWidth());
                    charHeight = (int) Math.round(temp.getItem().getLayoutBounds().getHeight());

                    editor.textLength += charWidth;
                    temp.setCharWidth(charWidth);

//editor.updateXY(lineLength){
//editor.positionX = (editor.textLength) % lineLength;
//editor.positionY = charHeight * ((editor.textLength) / lineLength) + 100;
//}
                    editor.positionX = (editor.textLength) % lineLength;
                    editor.positionY = charHeight * ((editor.textLength) / lineLength) + 100;
//cur.updateXY(curlength,lineLength,charHeight){
//editor.cur.setx(curLength % lineLength);
//editor.cur.sety(charHeight * (curLength / lineLength) + 100 - charHeight * 2 / 3);
//}
                    editor.cur.setx((editor.curLength) % lineLength);
                    editor.cur.sety(charHeight * ((editor.curLength) / lineLength) + 100 - charHeight * 2 / 3);

                    temp = temp.getNext();
                }

                keyEvent.consume();
            } else if (characterTyped.charAt(0) == 8) {

                FastLinkedListNode curdelete = editor.buffer.getCurrentNode();
                int charWidthDelete = (int) Math.round(curdelete.getItem().getLayoutBounds().getWidth());
                editor.curLength -= charWidthDelete;

                removeCurrent(2);
                editor.delete();
System.out.println();

                FastLinkedListNode temp = editor.buffer.getCurrentNode().getNext();
                while (temp!= null) {
                    Text displayText = temp.getItem();
                    System.out.println("add again"+displayText.getText());
                    displayText.setFont(Font.font(fontName, fontSize));

                    displayText.setX(editor.positionX);
                    displayText.setY(editor.positionY);

                    temp.updateXPos(editor.positionX);
                    temp.updateYPos(editor.positionY);

                    int charWidth = (int) Math.round(displayText.getLayoutBounds().getWidth());

                    editor.textLength += charWidth;

                    editor.positionX = (editor.textLength) % lineLength;
                    editor.positionY = charHeight * ((editor.textLength) / lineLength) + 100;

                    editor.cur.setx((editor.curLength) % lineLength);
                    editor.cur.sety(charHeight * ((editor.curLength) / lineLength) + 100 - charHeight * 2 / 3);
                    root.getChildren().add(displayText);
                    temp = temp.getNext();
                }
                keyEvent.consume();
            }

        } else if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
            // Arrow keys should be processed using the KEY_PRESSED event, because KEY_PRESSED
            // events have a code that we can check (KEY_TYPED events don't have an associated
            // KeyCode).


            KeyCode code = keyEvent.getCode();
            if (code == KeyCode.UP) {
                if (editor.curLength > lineLength) {

                    //editor.buffer.updateCurXY(editor.positionX, editor.positionY - charHeight);
                    FastLinkedListNode temp = editor.buffer.findNodewithXYForUpAndLeft(editor.positionX,
                            editor.positionY - charHeight);

                    //////get the current node and the position of the cursor from FastLinkedList
                    //editor.positionX = temp.getXPos();
                    //editor.positionY = temp.getYPos();
//should not be lineLength better get from FastLinkedList
                    editor.curLength -= lineLength;

                    editor.cur.setx((editor.curLength) % lineLength);
                    editor.cur.sety(charHeight * ((editor.curLength) / lineLength) + 100 - charHeight * 2 / 3);

                }
                //fontSize += 5;
                //displayText.setFont(Font.font(fontName, fontSize));
            } else if (code == KeyCode.DOWN) {
                //fontSize = Math.max(0, fontSize - 5);
                //displayText.setFont(Font.font(fontName, fontSize));
                if (editor.textLength - editor.curLength > lineLength) {
//should not be lineLength better get from FastLinkedList
//or could compute from the temp.getXPos and temp.getYPos
                    editor.curLength += lineLength;
                    //editor.buffer.updateCurXY(editor.positionX, editor.positionY + charHeight);
                    FastLinkedListNode temp = editor.buffer.findNodewithXYForDownAndRight(editor.positionX,
                            editor.positionY + charHeight);
                    //////get the current node and the position of the cursor from FastLinkedList

                    //editor.positionX = temp.getXPos();
                    //editor.positionY = temp.getYPos();
                    editor.cur.setx((editor.curLength) % lineLength);
                    editor.cur.sety(charHeight * ((editor.curLength) / lineLength) + 100 - charHeight * 2 / 3);
                    //////get the current node and the position of the cursor from FastLinkedList

                }
                else{
                    editor.curLength = editor.textLength;
                    //editor.buffer.updateCurXY(editor.positionX, editor.positionY);
                    editor.positionX = (editor.textLength) % lineLength;
                    editor.positionY = charHeight * ((editor.textLength) / lineLength) + 100;
                    editor.cur.setx((editor.curLength) % lineLength);
                    editor.cur.sety(charHeight * ((editor.curLength) / lineLength) + 100 - charHeight * 2 / 3);
                }
            }
            if (code == KeyCode.RIGHT) {
                if (editor.curLength < lineLength) {
                    FastLinkedListNode temp = editor.buffer.getCurrentNode();
                    int charWidth = (int) Math.round(temp.getItem().getLayoutBounds().getWidth());
                    editor.curLength += charWidth;

                    //editor.positionX = temp.getXPos();
                    //editor.positionY = temp.getYPos();
                    editor.cur.setx((editor.curLength) % lineLength);
                    editor.cur.sety(charHeight * ((editor.curLength) / lineLength) + 100 - charHeight * 2 / 3);

                    temp = temp.getNext();
                    editor.buffer.updateCurrentNode(temp);
                }
            }
            if (code == KeyCode.LEFT) {
                if (editor.curLength > 0) {
                    FastLinkedListNode temp = editor.buffer.getCurrentNode();
                    int charWidth = (int) Math.round(temp.getItem().getLayoutBounds().getWidth());
                    editor.curLength -= charWidth;

                    //editor.positionX = temp.getXPos();
                    // editor.positionY = temp.getYPos();
                    editor.cur.setx((editor.curLength) % lineLength);
                    editor.cur.sety(charHeight * ((editor.curLength) / lineLength) + 100 - charHeight * 2 / 3);

                    temp = temp.getPrev();
                    editor.buffer.updateCurrentNode(temp);
                }
            }
        }
    }
}
