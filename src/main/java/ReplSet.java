
public interface ReplSet<T> {
    /**
     * Menambahkan elemen ke dalam set
     *
     * @param obj
     * @return true jika object belum ada di dalam set,
     * false jika object ada di dalam set.
     */
    public boolean add(T obj);

    /**
     * Memeriksa keberadaan object dalam set
     *
     * @param obj
     * @return true jika object ada di dalam set
     */
    public boolean contains(T obj);

    /**
     * Menghapus elemen dari set
     *
     * @param obj
     * @return true jika object telah berada di dalam set,
     *  false jika object tidak ada dalam set
     */
    public boolean remove(T obj);
}
