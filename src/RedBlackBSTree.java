import java.util.LinkedList;
import java.util.Queue;

public class RedBlackBSTree {
    private static final boolean RED = false; // цвета нод будем обозначать true/false
    private static final boolean BLACK = true;
    private int operationsCount;
    private Node root; // корень дерева

    public RedBlackBSTree() {
        this.operationsCount = 0;
    }
    public int getOperationsCount() {
        return operationsCount;
    }

    // поиск
    // такой же, как и в бинарном дереве
    public Node searchNode(int key) {
        operationsCount = 0;
        Node node = root;
        while (node != null) {
            operationsCount += 1;
            if (key == node.getValue()) {
                return node;
            } else if (key < node.getValue()) {
                node = node.getLeft();
            } else {
                node = node.getRight();
            }
        }
        return null; // если такое значение не нашлось
    }

    // вставка элемента

    public void insertNode(int key) {
        operationsCount = 0;
        Node node = root;
        Node parent = null;

        // ищем, куда нам нужно вставить новый элемент
        while (node != null) {
            parent = node;
            if (key < node.getValue()) {
                operationsCount += 1;
                node = node.getLeft();
            } else if (key > node.getValue()) {
                node = node.getRight();
                operationsCount += 1;
            } else {
                throw new IllegalStateException("BST already contains a node with key " + key);
            }
        }

        // вставляем ноду
        Node newNode = new Node(key, RED); // устанавливаем цвет красным, когда создаем новую ноду, чтобы все пути от корня до листьев именли одинаковое кол-во чёрных нод
        if (parent == null) {
            root = newNode; // если изначально дерево было пустым
        } else if (key < parent.getValue()) {
            parent.setLeft(newNode);
        } else {
            parent.setRight(newNode);
        }
        operationsCount += 1;
        newNode.setParent(parent);
        // далее мы должны сделать балансировку дерева, чтобы все условия красно-чёрного выполнялись
        fixRedBlackPropertiesAfterInsert(newNode);
    }


    // вспомагательные функции

    private void fixRedBlackPropertiesAfterInsert(Node node) {
        Node parent = node.getParent();

        // 1 случай parent = null, значит, мы достигли корня, конец рекурсии
        if (parent == null) {
            operationsCount += 1;
            node.setColor(BLACK); // по 2 правилу мы считаем что корень всегда чёрный
            return;
        }

        // если parent чёрного цвета, то ничего делать не нужно
        if (parent.isColor() == BLACK) {
            operationsCount += 1;
            return;
        }

        // с этого момента мы считаем, что parent красного цвета
        Node grandparent = parent.getParent();

        /*// 2 случай
        // Если grandparent = null, то это значит, что parent корень. Но так как мы
        // указали, что корень всегда чёрный, то это условие бесполезно
        if (grandparent == null) {
            // As this method is only called on red nodes (either on newly inserted ones - or -
            // recursively on red grandparents), all we have to do is to recolor the root black.
            parent.setColor(BLACK);
            return;
        }*/

        Node uncle = getUncle(parent);

        // 3 случай дядя красный -> изменяем цвет у дяди, ролителя и дедушки
        if (uncle != null && uncle.isColor() == RED) {
            operationsCount += 1;
            parent.setColor(BLACK);
            grandparent.setColor(RED);
            uncle.setColor(BLACK);

            // тк дедушка теперь красный, то рекурсивно вызываем функцию для него
            // он может быть корнем или иметь красного родителя
            fixRedBlackPropertiesAfterInsert(grandparent);

            // иначе есля дядя черного цвета и родитель - левый ребенок дедушки
        } else if (parent == grandparent.getLeft()) {
            // случай 4а - дядя чёрный и нода - левый-правый внетренний внук (левый правый треугольник)
            if (node == parent.getRight()) {
                operationsCount += 1;
                rotateLeft(parent);
                parent = node; // родитель теперь указывает на новый корень повёрнутого поддерева

            }
            // случай 5а дядя узел черный, вставленный узел - "внешний внук" левый - левый
            rotateRight(grandparent);
            parent.setColor(BLACK);
            grandparent.setColor(RED);
            operationsCount += 1;
        }
        // родитель - правый ребёнок дедушки
        else {
            // случай 4b - дядя чёрный и нода правый левый внутренний внук (правый левый треугольник)
            if (node == parent.getLeft()) {
                operationsCount += 1;
                rotateRight(parent);
                parent = node;
            }
            // случай 5б дядя узел чёрный, вставленный узел - "внешний внук" правый - правый
            rotateLeft(grandparent);

            parent.setColor(BLACK);
            grandparent.setColor(RED);
        }
    }

    // удаление
    public void deleteNode(int key) {
        operationsCount = 0;
        Node node = root;

        // находим тот элемент, который нужно удалить
        while (node != null && node.getValue() != key) {
            operationsCount += 1;
            if (key < node.getValue()) {
                node = node.getLeft();
            } else {
                node = node.getRight();
            }
        }
        if (node == null) return; // не нашли значение

        // В этой переменной мы сохраним узел, с которого начнем исправлять наше дерево
        // после удаления узла.
        Node movedUpNode;
        boolean deletedNodeColor;

        // у узла один ребенок или нет детей
        if (node.getLeft() == null || node.getRight() == null) {
            operationsCount += 1;
            movedUpNode = deleteNodeWithZeroOrOneChild(node);
            deletedNodeColor = node.isColor();
        }

        // у ноды 2 ребёнка
        else {
            // ищем минимум в правом поддереве
            // (это будет последовательный преемник ноды)
            operationsCount += 1;
            Node inOrderSuccessor = findMinimum(node.getRight());
            // в нашу ноду которую нужно удалить переписываем значение inOrderSuccessor
            node.setValue(inOrderSuccessor.getValue());
            // удаляем inOrderSuccessor так же как бы мы удалилу ноду с 1 или 0 ребенком
            movedUpNode = deleteNodeWithZeroOrOneChild(inOrderSuccessor);
            deletedNodeColor = inOrderSuccessor.isColor();
        }

        if (deletedNodeColor == BLACK) {
            fixRedBlackPropertiesAfterDelete(movedUpNode);
            operationsCount += 1;
            if (movedUpNode.getClass() == NilNode.class) {
                replaceParentsChild(movedUpNode.getParent(), movedUpNode, null);
            }
        }
        //удаление красного узла не нарушает никаких правил.
        // Однако, если удаленный узел черный, мы вызываем
        // метод fixRedBlackPropertiesAfterDelete, исправляющий свойства
        // дерева после удаления.
        //Если таковые имеются, то временный заполнитель нулевого узла, созданный в deleteNodeWithZeroOrOneChild(),
        // понадобился нам только для вызова функции восстановления. Поэтому мы можем удалить его позже.
    }

    // метод, который обрабатывает случай для 1 ребенка или если нет детей
    private Node deleteNodeWithZeroOrOneChild(Node node) {
        // Узел имеет ТОЛЬКО левый дочерний элемент -> заменить на его левый дочерний элемент
        if (node.getLeft() != null) {
            operationsCount += 1;
            replaceParentsChild(node.getParent(), node, node.getLeft());
            return node.getLeft(); // перемещенный вверх узел
        } else if (node.getRight() != null) { // тоже самое для правого дочернего элемента
            operationsCount += 1;
            replaceParentsChild(node.getParent(), node, node.getRight());
            return node.getRight();
        }

        // если у ноды нет ни одного ребёнка
        // нода красная - просто удалить ее
        // нода чёрная - заменить ее на временную NIL ноду(она нужна, чтобы сбалансировать дерево)
        // Если у удаленного узла нет дочерних узлов, один
        // из его NIL элементов фактически перемещается на
        // свое место. Чтобы иметь возможность перейти от этого
        // нулевого элемента к его родительскому узлу позже,
        // нам нужен специальный "контейнер". Он реализован в классе Nullnode,
        else {
            operationsCount += 1;
            Node newChild = node.isColor() == BLACK ? new NilNode() : null;
            replaceParentsChild(node.getParent(), node, newChild);
            return newChild;
            // Наконец, метод перемещенную вверх ноду,
            // которую вызывающий метод deleteNode() сохраняе
            // в переменной movedUpNode.
        }
    }

    private void fixRedBlackPropertiesAfterDelete(Node node) {
        if (node == root) {
            // 1 случай - нода является корнем
            node.setColor(BLACK);
            return;
        }
        Node sibling = getSibling(node);

        // 2 случай - брат красный
        if (sibling.isColor() == RED) {
            operationsCount += 1;
            handleRedSibling(node, sibling);
            sibling = getSibling(node); // получаем нового брата для случая 3-6
        }
        // случай 3+4 чёрный брат с 2 чёрными детьми + красный родитель
        if (isBlack(sibling.getLeft()) && isBlack(sibling.getRight())) {
            sibling.setColor(RED);
            // случай 3 еще + красный родитель
            if (node.getParent().isColor() == RED) {
                node.getParent().setColor(BLACK);
            }
            // 4 случай чёрный брат с 2 черными детьми а  родитель чёрный
            else {
                fixRedBlackPropertiesAfterDelete(node.getParent());
            }
            operationsCount += 1;
        }
        // Случай 5+6 у чёрного брата есть по крайней мере 1 красный ребёнок
        else {
            handleBlackSiblingWithAtLeastOneRedChild(node, sibling);
        }

    }


    private Node getSibling(Node node) { // ищем брата
        operationsCount += 1;
        Node parent = node.getParent();
        if (node == parent.getLeft()) {
            return parent.getRight();
        } else if (node == parent.getRight()) {
            return parent.getLeft();
        } else {
            throw new IllegalStateException("Parent is not a child of its grandparent");
        }
    }

    private boolean isBlack(Node node) {
        operationsCount += 1;
        return node == null || node.isColor() == BLACK;
    }

    private Node findMinimum(Node node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
            operationsCount += 1;
        }
        return node;
    }

    private static class NilNode extends Node {
        private NilNode() {
            super(0, BLACK);
        }
    }

    private Node getUncle(Node parent) { // метод по которому мы получаем дядю для текущего родителя
        Node grandParent = parent.getParent();
        if (grandParent.getLeft() == parent) {
            operationsCount += 1;
            return grandParent.getRight();
        } else if (grandParent.getRight() == parent) {
            operationsCount += 1;
            return grandParent.getLeft();
        } else {
            throw new IllegalStateException("Parent is not a child of its grandparent");
        }
    }

    private void rotateRight(Node node) { // правый поворот
        Node parent = node.getParent();
        Node leftChild = node.getLeft();

        node.setLeft(leftChild.getRight());
        if (leftChild.getRight() != null) {
            leftChild.getRight().setParent(node);
            operationsCount += 1;
        }

        leftChild.setRight(node);
        node.setParent(leftChild);

        replaceParentsChild(parent, node, leftChild);
        operationsCount += 1;
    }

    private void rotateLeft(Node node) { // левый поворот
        Node parent = node.getParent();
        Node rightChild = node.getRight();

        node.setRight(rightChild.getLeft());
        operationsCount += 1;
        if (rightChild.getLeft() != null) {
            rightChild.getLeft().setParent(node);
            operationsCount += 1;
        }

        rightChild.setLeft(node);
        node.setParent(rightChild);

        replaceParentsChild(parent, node, rightChild);
    }

    private void replaceParentsChild(Node parent, Node oldChild, Node newChild) {
        // в этом методе мы меняем ссылку на ребёнка, который меняется при повороте
        operationsCount += 1;
        if (parent == null) {
            root = newChild; // если oldChild был корнем
        } else if (parent.getLeft() == oldChild) {
            parent.setLeft(newChild);
        } else if (parent.getRight() == oldChild) {
            parent.setRight(newChild);
        } else {
            throw new IllegalStateException("Node is not a child of its parent");
        }
        // дальше мы переприсваиваем ссылку на родителя ноде newChild
        if (newChild != null) {
            newChild.setParent(parent);
        }
    }

    private void handleRedSibling(Node node, Node sibling) {
        sibling.setColor(BLACK);
        node.getParent().setColor(RED);
        if (node == node.getParent().getLeft()) {
            rotateLeft(node.getParent());
        } else {
            rotateRight(node.getParent());
        }
        operationsCount += 1;
    }

    private void handleBlackSiblingWithAtLeastOneRedChild(Node node, Node sibling) {
        boolean nodeIsLeftChild = node == node.getParent().getLeft();
        // 5 случай: брат черный с хотя бы 1 красным ребенком, внешний племянник чёрный
        if (nodeIsLeftChild && isBlack(sibling.getRight())) {
            operationsCount += 1;
            sibling.getLeft().setColor(BLACK);
            sibling.setColor(RED);
            rotateRight(sibling);
            sibling = node.getParent().getRight();
        } else if (!nodeIsLeftChild && isBlack(sibling.getLeft())) {
            operationsCount += 1;
            sibling.getRight().setColor(BLACK);
            sibling.setColor(RED);
            rotateLeft(sibling);
            sibling = node.getParent().getLeft();
        }

        // тут же переходим к 6 случаю, так как продолжение случая 5 - это случай 6
        // брат чёрный, у него по крайней мере 1 красный ребенок, внешний племянник красный
        sibling.setColor(node.getParent().isColor());
        node.getParent().setColor(BLACK);
        if (nodeIsLeftChild) {
            operationsCount += 1;
            sibling.getRight().setColor(BLACK);
            rotateLeft(node.getParent());
        } else {
            operationsCount += 1;
            sibling.getLeft().setColor(BLACK);
            rotateRight(node.getParent());
        }
    }

    public void print() { // вывод дерева
        Node root = this.root;
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node curr = queue.poll();
            System.out.print(curr.getValue() + " " + (curr.isColor() == RED) + " ");
            if (curr.getLeft() != null) {
                queue.add(curr.getLeft());
            }
            if (curr.getRight() != null) {
                queue.add(curr.getRight());
            }
        }
    }
}
