public class QuickUnionUFW {

    private int[] id;
    private int[] size;

    public QuickUnionUFW(int N) {
        id = new int[N];
        size = new int[N];
        for (int i=0; i<N; i++)
        {
            id[i] = i;
            size[i] = 1;
        }
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
        if (size[i] < size[j])
        {
            id[i] = j;
            size[j] += size[i];
        }
        else
        {
            id[j] = i;
            size[i] += size[j];
        }

    }

    public static void main(String [] args)
    {
        int n=256;
        QuickUnionUFW ufw = new QuickUnionUFW(n);

        for(int i=0; i<n-1; i=i+2)
        {
            ufw.union(i, i+1);
        }

        for(int i=0; i<n-55; i=i+3)
        {
            ufw.union(i, i+3);
        }

        System.out.println(ufw.find(19));
        System.out.println(ufw.find(112));

    }
}
