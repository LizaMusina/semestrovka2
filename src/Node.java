public class Node {
    private int value;
    private Node left, right;
    private boolean color;
    private Node parent;

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public void setColor(boolean color) {
        this.color = color;
    }


    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public boolean isColor() {
        return color;
    }

    public Node(int value, boolean color) {
        this.value = value;
        this.color = color;
        this.left = null;
        this.right = null;
        this.parent = null;
    }
}
