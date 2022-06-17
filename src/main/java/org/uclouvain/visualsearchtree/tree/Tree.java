package org.uclouvain.visualsearchtree.tree;

// https://www.microsoft.com/en-us/research/wp-content/uploads/1996/01/drawingtrees.pdf

import java.util.*;


public class Tree {

    public static void main(String[] args) {

        /*
        Node<String> left = new Node<>("left",List.of(),null,null);
        Node<String> right = new Node<>("right",List.of(),null,null);

        Node<String> root = new Node<>("root", List.of(left,right),null,null);
        //Node<String> root = new Node<>("root", List.of(),null,null);
        */
        Node<String> root = randomTree(0);

        PositionedNode<String> proot = root.design();

        System.out.println(proot);


    }

    static Random rand = new Random(1);

    public static Tree.Node<String> randomTree() {
        return staticTree();
        //return randomTree(0);
    }

    private static Tree.Node<String> randomTree(int depth) {

        int nChildren = 2;
        String branch = "BRANCH";

        if ((rand.nextInt(15) < 10 && depth > 3)) {
            nChildren = 0;
        }
        int nRandom = rand.nextInt(3);
        if (nRandom < 2 && nChildren==0) {
            branch = "FAILED";
        }
        if (nRandom >= 2 && nChildren==0) {
            branch = "SOLVED";
        }

        List<Tree.Node<String>> children = new LinkedList<>();
        List<String> labels = new LinkedList<>();

        for (int i = 0; i < nChildren; i++) {
            children.add(randomTree(depth + 1));
            System.out.println(children);
            labels.add("x = " + i);
        }
        return new Tree.Node<String>("Node" + depth, children, labels, null, branch);
    }

    public static Tree.Node<String> staticTree(){
        Node<String> node11 = new Tree.Node<>("Node 11", new LinkedList<>(), new LinkedList<>(), null, "FAILED");
        Node<String> node12 = new Tree.Node<>("Node 12", new LinkedList<>(), new LinkedList<>(), null, "FAILED");

        Node<String> node9 = new Tree.Node<>("Node 9", new LinkedList<>(), new LinkedList<>(), null, "SOLVED");
        Node<String> node10 = new Tree.Node<>("Node 10", new LinkedList<>(), new LinkedList<>(), null, "FAILED");

        Node<String> node8 = new Tree.Node<>("Node 8", new LinkedList<>(), new LinkedList<>(), null, "SOLVED");
        Node<String> node7 = new Tree.Node<>("Node 7", new LinkedList<>(), new LinkedList<>(), null, "BRANCH");
        Node<String> node5 = new Tree.Node<>("Node 5", new LinkedList<>(), new LinkedList<>(), null, "BRANCH");
        Node<String> node4 = new Tree.Node<>("Node 4", new LinkedList<>(), new LinkedList<>(), null, "FAILED");
        Node<String> node3 = new Tree.Node<>("Node 3", new LinkedList<>(), new LinkedList<>(), null, "FAILED");

        // Node 5 children
        node5.children.add(node11);
        node5.children.add(node12);

        // Node 7 children
        node7.children.add(node9);
        node7.children.add(node10);

        //Node 6 children
        List<Tree.Node<String>> children6 = new LinkedList<>();
        children6.add(node7);
        children6.add(node8);

        //label 6
        List<String> label6 = new LinkedList<>();
        label6.add("x = "+0);
        label6.add("x ="+1);

        Node<String> node6 = new Tree.Node<>("Node 6", children6, label6, null, "BRANCH");

        //Node 2 children
        List<Tree.Node<String>> children2 = new LinkedList<>();
        children2.add(node5);
        children2.add(node6);

        //label 2
        List<String> label2 = new LinkedList<>();
        label2.add("x = "+0);
        label2.add("x ="+1);

        Node<String> node2 = new Tree.Node<>("Node 2", children2, label2, null, "BRANCH");

        //Node 1 children
        List<Tree.Node<String>> children1 = new LinkedList<>();
        children1.add(node3);
        children1.add(node4);

        //label 1
        List<String> label1 = new LinkedList<>();
        label1.add("x = "+0);
        label1.add("x ="+1);

        Node<String> node1 = new Tree.Node<>("Node 2", children1, label1, null, "BRANCH");

        //Node 0 children
        List<Tree.Node<String>> children0 = new LinkedList<>();
        children0.add(node1);
        children0.add(node2);

        //label 0
        List<String> label0 = new LinkedList<>();
        label0.add("x = "+0);
        label0.add("x ="+1);


        return new Tree.Node<>("staticTree", children0, label0, null, "BRANCH");

    }

    static record Pair<L, R>(L left, R right) {

    }

    @FunctionalInterface
    interface NodeAction {
        void nodeAction();
    }

    public static class Node<T> {
        public int nodeId;
        public int nodePid;
        public T label;
        public List<Node<T>> children;
        public List<T> edgeLabels;


        @Override
        public String toString() {
            return "Node [" +
                    "label=" + label +
                    ", children=" + children +
                    ", edgeLabels=" + edgeLabels +
                    ", onClick=" + onClick +
                    ", branch=" + branch +
                    ']';
        }

        public NodeAction onClick;
        public String branch;

        public Node(){

        }

        public Node(T label, List<Node<T>> children, List<T> edgeLabels, NodeAction onClick, String branch ) {
            this.label = label;
            this.children = children;
            this.edgeLabels = edgeLabels;
            this.onClick = onClick;
            this.branch = branch;

        }

        public Node(int nodeId, int nodePid, T label, List<Node<T>> children, List<T> edgeLabels, NodeAction onClick, String branch ) {
            this.nodeId = nodeId;
            this.nodePid = nodePid;
            this.label = label;
            this.children = children;
            this.edgeLabels = edgeLabels;
            this.onClick = onClick;
            this.branch = branch;

        }

        public PositionedNode<T> design() {
            Pair<PositionedNode<T>, Extent> res = design_();
            return res.left();
        }

        private Pair<PositionedNode<T>, Extent> design_() {
            List<PositionedNode<T>> subtrees = new LinkedList<>();
            List<Extent> subtreeExtents = new LinkedList<>();
            for (Node<T> child : children) {
                Pair<PositionedNode<T>, Extent> res = child.design_();
                subtrees.add(res.left());
                subtreeExtents.add(res.right());
            }
            List<Double> positions = Extent.fitList(subtreeExtents);

            List<PositionedNode<T>> subtreesMoved = new LinkedList<>();
            List<Extent> extentsMoved = new LinkedList<>();

            Iterator<PositionedNode<T>> childIte = subtrees.iterator();
            Iterator<Extent> extentIte = subtreeExtents.iterator();
            Iterator<Double> posIte = positions.iterator();

            while (childIte.hasNext() && posIte.hasNext() && extentIte.hasNext()) {

                double pos = posIte.next();
                subtreesMoved.add(childIte.next().moveTree(pos));
                extentsMoved.add(extentIte.next().move(pos));
            }

            Extent resExtent = Extent.merge(extentsMoved);
            resExtent.addFirst(0, 0);

            PositionedNode<T> resTree = new PositionedNode<T>(label, subtreesMoved, edgeLabels, onClick, 0,branch);
            return new Pair(resTree, resExtent);
        }

        public void addChildren(Node<T> newChild) {
            children.add(newChild);
        }

        public T getLabel() {
            return label;
        }
        public int getNodeId() {
            return nodeId;
        }
        public int getNodePid() {
            return nodePid;
        }
        public NodeAction getOnClick() {
            return onClick;
        }
        public String getBranch() {
            return branch;
        }
    }

    public static class PositionedNode<T> implements NodeAction{

        public double position;
        public T label;
        String branch;
        public List<PositionedNode<T>> children;
        public List<T> edgeLabels;
        public NodeAction onClick;

        public PositionedNode(T label, List<PositionedNode<T>> children, List<T> edgeLabels, NodeAction onClick, double position, String branch) {
            this.label = label;
            this.children = children;
            this.edgeLabels = edgeLabels;
            this.onClick = onClick;
            this.position = position;
            this.branch = branch;

        }

        public PositionedNode moveTree(double x) {
            return new PositionedNode(label, children, edgeLabels, onClick, position + x, branch);
        }

        @Override
        public String toString() {
            return "PositionedNode{" +
                    "position=" + position +
                    ", label=" + label +
                    ", branch=" + branch +
                    ", children=" + children +
                    ", edgeLabels=" + edgeLabels +
                    ", onClick=" + onClick +
                    '}';
        }

        @Override
        public void nodeAction() {
            System.out.println("Clicked"+ this);
        }
    }

    static class Extent {

        double minDist = 1.0;

        List<Pair<Double, Double>> extentList;

        public Extent() {
            this(new LinkedList<Pair<Double, Double>>());
        }

        public Extent(List<Pair<Double, Double>> extentList) {
            this.extentList = extentList;
        }

        public Extent(double left, double right) {
            List.of(new Pair(left, right));
        }


        public boolean isEmpty() {
            return extentList.isEmpty();
        }

        public void add(double x1, double x2) {
            extentList.add(new Pair(x1, x2));
        }

        public void addFirst(double x1, double x2) {
            extentList.add(0, new Pair(x1, x2));
        }

        public Extent move(double x) {
            return new Extent(extentList.stream().map(p -> new Pair<Double, Double>(p.left() + x, p.right() + x)).toList());
        }


        public Extent merge(Extent other) {

            List<Pair<Double, Double>> f = extentList;
            List<Pair<Double, Double>> s = other.extentList;
            List<Pair<Double, Double>> r = new LinkedList<>();

            Iterator<Pair<Double, Double>> fi = f.iterator();
            Iterator<Pair<Double, Double>> si = s.iterator();

            while (fi.hasNext() && si.hasNext()) {
                r.add(new Pair(fi.next().left(), si.next().right()));
            }

            if (!fi.hasNext()) {
                while (si.hasNext()) {
                    r.add(si.next());
                }
            }

            if (!si.hasNext()) {
                while (fi.hasNext()) {
                    r.add(fi.next());
                }
            }
            return new Extent(r);
        }

        public static Extent merge(List<Extent> extents) {
            Extent r = new Extent(); // empty
            for (Extent e : extents) {
                r = r.merge(e);
            }
            return r;
        }

        public Double fit(Extent other) {
            List<Pair<Double, Double>> f = extentList;
            List<Pair<Double, Double>> s = other.extentList;

            Iterator<Pair<Double, Double>> fi = f.iterator();
            Iterator<Pair<Double, Double>> si = s.iterator();

            Double minDist = 0.0;

            while (fi.hasNext() && si.hasNext()) {
                minDist = Math.max(minDist, fi.next().right() - si.next().left() +1);
            }
            return minDist;

        }

        public static List<Double> fitListLeft(List<Extent> extents) {
            List<Double> res = new LinkedList<>();
            Extent acc = new Extent();
            for (Extent e : extents) {
                double x = acc.fit(e);
                res.add(x);
                acc = acc.merge(e.move(x));
            }
            return res;
        }

        public static List<Double> fitListRight(List<Extent> extents) {
            Collections.reverse(extents);
            List<Double> res = new LinkedList<>();
            Extent acc = new Extent();
            for (Extent e : extents) {
                double x = -e.fit(acc);
                res.add(x);
                acc = e.move(x).merge(acc);
            }
            Collections.reverse(extents);
            Collections.reverse(res);
            return res;
        }

        public static List<Double> fitList(List<Extent> extents) {
            List<Double> left = fitListLeft(extents);
            List<Double> right = fitListRight(extents);
            List<Double> res = new LinkedList<>();
            for (Iterator<Double> leftIte = left.iterator(), rightIte = right.iterator(); leftIte.hasNext() && rightIte.hasNext(); ) {
                res.add((leftIte.next() + rightIte.next()) / 2);
            }
            return res;
        }

    }
}





