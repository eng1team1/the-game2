package main.game.world.player.Objectives;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ObjectiveManager implements List<Objective> {
    private List<Objective> objectives;

    public ObjectiveManager(List<Objective> objectives) {
        this.objectives = objectives;
    }

    /**
     * Updates an {@link Objective} with a given key, if there is an {@link Objective} to modify.
     * @param update the {@link Objective} key.
     * @param amount to increase the {@link Objective} count.
     * @return the xp from the current {@link Objective}, if this {@link Objective} was completed.
     */
    public int updateObjective(String update, int amount) {
        if (objectives.size() != 0) {
            int potentialXp = objectives.get(0).getXp();
            if (objectives.get(0).updateObjective(update, amount)) {
                changeObjectives();
                return potentialXp;
            }
        }

        return 0;
    }

    /**
     * When an objective is completed remove it from the list.
     */
    private void changeObjectives() {
        objectives.remove(0);
    }

    public Objective getCurrentObjective() {
        if (objectives == null) return null;
        if (objectives.size() != 0) return objectives.get(0);
        else return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<Objective> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(Objective objective) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Objective> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Objective> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Objective get(int index) {
        return null;
    }

    @Override
    public Objective set(int index, Objective element) {
        return null;
    }

    @Override
    public void add(int index, Objective element) {

    }

    @Override
    public Objective remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<Objective> listIterator() {
        return null;
    }

    @Override
    public ListIterator<Objective> listIterator(int index) {
        return null;
    }

    @Override
    public List<Objective> subList(int fromIndex, int toIndex) {
        return null;
    }
}
