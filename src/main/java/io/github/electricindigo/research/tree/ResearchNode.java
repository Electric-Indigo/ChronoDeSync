package io.github.electricindigo.research.tree;

import java.util.List;

public record ResearchNode(String id, String title, String description, String infoText,
                           int x, int y, List<String> prerequisites)
{
    public boolean hasInfoPage()
    {
        return !infoText.isEmpty();
    }
}
