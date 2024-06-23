package is.interprete;

import is.command.HistoryCommandHandler;
import is.command.MoveCommand;
import is.command.MoveOffsetCommand;
import is.shapes.model.GraphicObject;
import is.shapes.view.GraphicObjectPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MoveExpression implements Expression {
    private String objId;
    private String pos;
    private boolean isOffset;

    private HistoryCommandHandler handler;
    private GraphicObjectPanel gpanel;

    private List<GraphicObject> graphicList = new ArrayList<>();

    private String[] tokens;

    public MoveExpression(String[] tokens,HistoryCommandHandler handler,GraphicObjectPanel gpanel,List<GraphicObject> graphicList) {
        this.tokens=tokens;
        this.handler=handler;
        this.gpanel=gpanel;
        this.graphicList=graphicList;
    }

    @Override
    public void interpret(Context context) {
        try{
            this.objId = tokens[1];
            this.pos = tokens[2];
            this.isOffset = tokens[0].equals("moveOff");
            boolean found = graphicList.stream()
                    .anyMatch(obj -> obj.getId() == Integer.parseInt(objId));
            if(!found){
                throw new IdNotFoundExceptio();
            }
            if (isOffset) {
                context.addOutput("Moving object " + objId + " by offset " + pos);
                String[] offsets = pos.split(",");
                for(GraphicObject graphicObject:graphicList) {
                    if (graphicObject.getId() == Integer.parseInt(objId)) {
                        MoveOffsetCommand moveOffsetCommand = new MoveOffsetCommand(graphicObject, offsets);
                        handler.handle(moveOffsetCommand);
                        gpanel.repaint();
                    }
                }
            } else {
                context.addOutput("Moving object " + objId + " to position " + pos);
                String[] coords = pos.split(",");
                Point newPoint = new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
                for(GraphicObject graphicObject:graphicList) {
                    if (graphicObject.getId() == Integer.parseInt(objId)) {
                        MoveCommand moveCommand = new MoveCommand(graphicObject, newPoint);
                        handler.handle(moveCommand);
                        gpanel.repaint();
                    }
                }
            }
        }catch (IdNotFoundExceptio id){
            JOptionPane.showMessageDialog(
                    null,
                    "ID Non Esistente",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }catch (Exception e){
            JOptionPane.showMessageDialog(
                    null,
                    "Errore input\n"+e,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }
}