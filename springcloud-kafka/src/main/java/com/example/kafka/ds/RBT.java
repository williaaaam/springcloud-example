package com.example.kafka.ds;

/**
 * https://www.bilibili.com/video/BV1qK4y1M7Fg?p=13&spm_id_from=pageDriver
 *
 * @author Williami
 * @description
 * @date 2022/3/15
 */
public class RBT<K extends Comparable<K>, V> {

    private static final boolean RED = false;
    private static final boolean BLACK = false;

    private TNode root;


    /**
     * 删除节点
     *
     * @param key
     * @return
     */
    public V remove(K key) {
        // 先根据key找到对应的节点
        TNode node = get(key);
        if (node == null) {
            return null;
        }
        V oldValue = (V) node.v;
        deleteNode(node);
        return oldValue;
    }

    /**
     * 3种情况：
     * 1. 删除叶子节点，直接删除
     * 2. 删除节点有一个子节点，由子节点来替代
     * 3. 删除节点有两个子节点，那么此时我们需要获取对应的前驱或者后继节点来替代（可以转换为1,2的情况）
     *
     * @param node
     */
    private void deleteNode(TNode node) {
        if (leftOf(node) != null && rightOf(node) != null) {// 存在左右子节点
            TNode successor = successor(node);
            // 使用后继节点覆盖要删除节点的值
            node.k = successor.k;
            node.v = successor.v;
            // 删除节点就变成后继节点
            node = successor;
        }

        TNode replacement = node.left != null ? node.left : node.right;
        if (replacement != null) {
            // 删除有一个子节点的情况
            replacement.parent = node.parent;
            if (node.parent == null) {
                root = replacement;
            } else if (node == leftOf(parentOf(node))) {
                parentOf(node).left = replacement;
            } else {
                parentOf(node).right = replacement;
            }
            // 将node左右孩子和父指针都向null, wait gc
            node.left = node.right = node.parent = null;
            // 替换完成后，调整平衡
            if (node.color == BLACK) {
                fixAfterRemove(replacement);
            }
        } else if (node.parent == null) {
            root = null;
        } else {
            // 删除的是叶子节点
            // 先调整
            if (node.color == BLACK) {
                fixAfterRemove(node);
            }
            // 再删除
            if (node.parent != null) {
                if (node == leftOf(parentOf(node))) {
                    parentOf(node).left = null;
                } else {
                    parentOf(node).right = null;
                }
                node = null;
            }
        }
    }

    /**
     * 删除后的调整处理
     * 2-3-4树删除操作
     *
     * @param x 替代节点
     */
    private void fixAfterRemove(TNode x) {
        if (x != root && colorOf(x) == BLACK) { // x要是红色，直接染成黑色
            if (x == leftOf(parentOf(x))) { //  x是父节点的左节点
                // 查找兄弟接地
                TNode tNode = rightOf(parentOf(x));
                // 1. 跟兄弟借，兄弟节点有借
                // 2. 跟兄弟借，兄弟没得借
            } else {

            }
        }
        // 替代节点是红色，直接设置为黑色
        setColor(x, BLACK);
    }

    private TNode get(K key) {
        TNode root = this.root;
        if (root == null) {
            return null;
        }
        int cmp = key.compareTo((K) root.k);
        if (cmp < 0) {
            root = root.left;
        } else if (cmp > 0) {
            root = root.right;
        } else {
            return root;
        }
        return root;
    }


    /**
     * 新增节点：
     * 1. 普通二叉树插入<br>
     * 先查询插入位置；
     * 封装节点；
     * 2. 红黑树的平衡：旋转+变色
     *
     * @param key
     * @param value
     */
    public void put(K key, V value) {
        TNode root = this.root;
        if (root == null) {
            this.root = new TNode(null, key, value);
            setColor(this.root, BLACK);
            return;
        }
        // 查找插入位置
        int cmp;
        if (key == null) {
            throw new NullPointerException();
        }
        // 记录寻找节点的父节点
        TNode parent;
        do {
            parent = root;
            cmp = key.compareTo((K) root.k);
            if (cmp < 0) {
                root = root.left;
            } else if (cmp > 0) {
                root = root.right;
            } else {
                // 值覆盖
                root.setV(value);
            }
        } while (root != null);

        // 找到了插入的位置
        TNode node = new TNode(parent, key, value);
        if (cmp < 0) {
            parent.left = node;
        } else {
            parent.right = node;
        }
        // 平衡处理 旋转和调色
        fixAfterInsert(node);
    }

    /**
     * 1. 2-3-4树 2节点 新增一个元素直接合并为3节点
     * 红黑树：新增一个红色节点，这个红色节点会添加在黑色节点下：不需要调整
     * <p>
     * 2. 2-3-4树 3节点 新增一个元素合并为一个4节点
     * 红黑树：6种情况(两种情况不需要调整)
     * 根左左 根佑佑 旋转一次
     * 根左右 根右左 旋转两次
     * <p>
     * 3. 2-3-4树 4节点 新增节点 4节点中间元素升级为父节点 新增元素和剩下节点合并
     * 红黑树：新增节点是红色+爷爷节点是黑色,且父和叔节点是黑色
     * 调整为爷爷节点为红色，父亲和叔叔节点是黑色；如果爷爷节点是root节点，则调整为黑色
     *
     * @param x
     */
    private void fixAfterInsert(TNode x) {
        // 新插入节点是红色
        setColor(x, RED);
        while (x != null && x != root && parentOf(x).color == RED) { // 新插入节点父亲节点是红色
            // x的父亲节点是x的爷爷节点的左节点
            if (parentOf(x) == parentOf(parentOf(x)).left) {
                TNode uncle = rightOf(parentOf(parentOf(x)));
                if (colorOf(uncle) == RED) { // 叔叔节点是红色，不用调整，只变色(因为当前节点是红色，RBT不允许出现连续两个红色节点，因此需要变色)
                    setColor(parentOf(x), BLACK); // 父亲节点变为黑色
                    setColor(uncle, BLACK); // 叔叔节点也变为黑色
                    setColor(parentOf(parentOf(x)), RED); // 爷爷节点变为红色
                    // 变色后将这一部分视为红色节点，根据爷爷节点继续递归调整
                    x = parentOf(parentOf(x));
                } else { // 叔叔节点是黑色
                    // 情况5：左右
                    if (x == rightOf(parentOf(x))) { // x是父亲节点的右节点
                        x = parentOf(x);
                        // 根据x的父亲节点进行右旋
                        rotateLeft(x);
                    }
                    // 情况3：左左
                    // x父亲节点设置为黑色
                    setColor(parentOf(x), BLACK);
                    // 爷爷节点设置为红色
                    setColor(parentOf(parentOf(x)), RED);
                    // 根据爷爷节点进行右旋
                    rotateRight(parentOf(parentOf(x)));
                }
            } else {
                TNode uncle = leftOf(parentOf(parentOf(x)));
                // 情况2 父亲节点和叔叔节点都是红色
                if (colorOf(uncle) == RED) { // 叔叔节点是红色，不用调整，只变色(因为当前节点是红色，RBT不允许出现连续两个红色节点，因此需要变色)
                    setColor(parentOf(x), BLACK); // 父亲节点变为黑色
                    setColor(uncle, BLACK); // 叔叔节点也变为黑色
                    setColor(parentOf(parentOf(x)), RED); // 爷爷节点变为红色
                    // 变色后将这一部分视为红色节点，根据爷爷节点继续递归调整
                    x = parentOf(parentOf(x));
                } else { // 叔叔节点是黑色
                    // 情况6：右左
                    if (x == leftOf(parentOf(x))) { // x是父亲节点的右节点
                        x = parentOf(x);
                        // 根据x的父亲节点进行右旋
                        rotateRight(x);
                    }
                    // x父亲节点设置为黑色
                    setColor(parentOf(x), BLACK);
                    // 爷爷节点设置为红色
                    setColor(parentOf(parentOf(x)), RED);
                    // 根据爷爷节点进行右旋
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }

        // 根节点设置为黑色
        setColor(root, BLACK);
    }

    /**
     * TreeMap删除节点使用的是后继节点替代删除的节点值
     * 查找前驱节点
     *
     * @param node
     * @return
     */
    private TNode predecessor(TNode node) {
        if (node == null) {
            return null;
        }
        if (node.left != null) {
            TNode p = node.left;
            while (p.right != null) {
                p = p.right;
            }
            return p;
        }
        // node不存在左节点
        // 这种情况在删除中不存在，但在前驱和后继节点情况来说还是会存在的
        TNode p = node.parent;
        TNode ch = node;
        while (p != null && ch == p.left) {
            ch = p;
            p = p.parent;
        }
        return p;
    }

    private TNode successor(TNode node) {
        if (node == null) {
            return null;
        }
        if (node.right != null) {
            TNode p = node.right;
            while (p.left != null) {
                p = p.left;
            }
            return p;
        }
        // node不存在左节点
        // 这种情况在删除中不存在，但在前驱和后继节点情况来说还是会存在的
        TNode p = node.parent;
        TNode ch = node;
        while (p != null && ch == p.right) {
            ch = p;
            p = p.parent;
        }
        return p;
    }

    /**
     * @param node
     * @return
     */
    private static boolean colorOf(TNode node) {
        return node == null ? BLACK : node.color;
    }

    private static TNode parentOf(TNode node) {
        return node != null ? node.parent : null;
    }

    private static TNode leftOf(TNode node) {
        return node != null ? node.left : null;
    }

    private static TNode rightOf(TNode node) {
        return node != null ? node.right : null;
    }

    private void setColor(TNode node, boolean color) {
        if (node != null) {
            node.setColor(color);
        }
    }


    /**
     * 左旋:p左子节点保持不显，右子节点变成p父节点，右子节点的左子节点变成p节点的右子节点
     *
     * @param p
     */
    private void rotateLeft(TNode p) {
        if (p != null) {
            TNode pr = p.right;
            // pr->rl变成p->rl
            p.right = pr.left;
            if (pr.left != null) {
                pr.left.parent = p;
            }
            // 判断p是否有父节点
            pr.parent = p.parent;
            if (p.parent == null) {
                root = pr;
            } else if (p.parent.left == p) { // p是左节点
                p.parent.left = pr;
            } else {
                p.parent.right = pr;
            }
            // 最后设置p为pr的左子节点
            pr.left = p;
            p.parent = pr;
        }
    }

    /**
     * 左旋
     *
     * @param p
     */
    private void rotateRight(TNode p) {
        if (p != null) {
            TNode pl = p.left;
            // pr->rl变成p->rl
            p.left = pl.right;
            if (pl.right != null) {
                pl.right.parent = p;
            }
            // 判断p是否有父节点
            pl.parent = p.parent;
            if (p.parent == null) {
                root = pl;
            } else if (p.parent.left == p) { // p是左节点
                p.parent.left = pl;
            } else {
                p.parent.right = pl;
            }
            // 最后设置p为pr的左子节点
            pl.right = p;
            p.parent = pl;
        }
    }


    // 节点类型
    static class TNode<K extends Comparable<K>, V> {
        private TNode parent;
        private TNode left;
        private TNode right;

        private K k;
        private V v;

        private boolean color;


        public TNode(TNode parent, TNode left, TNode right, K k, V v, boolean color) {
            this.parent = parent;
            this.left = left;
            this.right = right;
            this.k = k;
            this.v = v;
            this.color = color;
        }

        public TNode(TNode parent, K k, V v) {
            this.parent = parent;
            this.k = k;
            this.v = v;
        }

        public TNode getParent() {
            return parent;
        }

        public void setParent(TNode parent) {
            this.parent = parent;
        }

        public TNode getLeft() {
            return left;
        }

        public void setLeft(TNode left) {
            this.left = left;
        }

        public TNode getRight() {
            return right;
        }

        public void setRight(TNode right) {
            this.right = right;
        }

        public K getK() {
            return k;
        }

        public void setK(K k) {
            this.k = k;
        }

        public V getV() {
            return v;
        }

        public void setV(V v) {
            this.v = v;
        }

        public boolean isColor() {
            return color;
        }

        public void setColor(boolean color) {
            this.color = color;
        }
    }
}
