package io.github.electricindigo.research.puzzle.causality;

import java.util.List;

public sealed interface CausalityClue  permits CausalityClue.Before, CausalityClue.NotAdjacent
{
    boolean isSatisfied(List<CausalityEvent> order);

    record Before(CausalityEvent first, CausalityEvent second) implements CausalityClue
    {
        @Override
        public boolean isSatisfied(List<CausalityEvent> order) {
            return order.indexOf(first) < order.indexOf(second);
        }
    }

    record NotAdjacent(CausalityEvent a, CausalityEvent b) implements CausalityClue
    {
        @Override
        public boolean isSatisfied(List<CausalityEvent> order) {
            return Math.abs(order.indexOf(a) - order.indexOf(b)) !=1;
        }
    }
}
