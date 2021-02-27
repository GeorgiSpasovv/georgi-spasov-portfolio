public class QuickUnionUF {
    private int[] id;
    private int count;

    public QuickUnionUF(int N) {
        id = new int[N];
        for (int i=0; i<N; i++) id[i] = i;
    }
    private int root(int i) {
        while (i != id[i]) i = id[i];
        return i;
    }
    public boolean isConnected(int p, int q){
        return root(p) == root(q);
    }
    public int find(int p){
        return root(p);
    }
    public void union(int p, int q){
        int i = root(p);
        int j = root(q);
        id[i] = j;
        count++;
    }

    public int count1() {
        return count;
    }

    public static void main(String [] args)
    {
        int n=256;
        QuickUnionUF uf = new QuickUnionUF(n);

        for(int i=0; i<n-1; i=i+2)
        {
            uf.union(i, i+1);
        }

        for(int i=0; i<n-55; i=i+3)
        {
            uf.union(i, i+3);
        }

        System.out.println(uf.find(19));
        System.out.println(uf.find(112));
        System.out.println(uf.count1());
    }
}
