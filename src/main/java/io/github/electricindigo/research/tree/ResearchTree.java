package io.github.electricindigo.research.tree;

import org.jspecify.annotations.Nullable;

import java.util.*;

public final class ResearchTree
{
    private static final Map<String, ResearchNode> NODES = new LinkedHashMap<>();

    static
    {
        add(new ResearchNode("chronometry", "Chronometry",
                "The basics of measuring time drift.",
                "Every object drifts through time at a slightly different rate. The drift is tiny, "
        + "but a sensitive enough instrument can pick it up. \n"
        + "Chronometry is the study of measuring that drift, and it is the foundation "
        + "for everything else on this terminal.",
                0, 26, List.of()));
        add(new ResearchNode("chrono_chem", "Chrono-Chem",
                "Compounds that react differently across time.", "",
                90, 0, List.of("chronometry")));
        add(new ResearchNode("waveforms", "Waveforms",
                "Reading and syncing temporal signals.", "",
                90, 52, List.of("chronometry")));
        add(new ResearchNode("causality", "Causality",
                "Cause and effect, and how to break it.", "",
                180, 26, List.of("chrono_chem", "waveforms")));
        add(new ResearchNode("desync_field", "Desync Field",
                "Pull a pocket of space out of sync.", "",
                270, 26, List.of("causality")));
    }

    private ResearchTree(){}

    private static void add(ResearchNode node)
    {
        NODES.put(node.id(), node);
    }

    public static Collection<ResearchNode> all()
    {
        return Collections.unmodifiableCollection(NODES.values());
    }

    public static @Nullable ResearchNode get(String id)
    {
        return NODES.get(id);
    }

    public static NodeState stateOf(ResearchNode node, Set<String> unlocked)
    {
        if (unlocked.contains(node.id()))
        {
            return NodeState.UNLOCKED;
        }
        if (unlocked.containsAll(node.prerequisites()))
        {
            return NodeState.AVAILABLE;
        }

        for (String preId : node.prerequisites())
        {
            ResearchNode pre = get(preId);
            if (pre == null)
            {
                return NodeState.HIDDEN;
            }
            boolean preUnlocked = unlocked.contains(preId);
            boolean preAvailable = unlocked.containsAll(pre.prerequisites());
            if (!preUnlocked && !preAvailable)
            {
                return NodeState.HIDDEN;
            }
        }
        return NodeState.PREVIEW;
    }
}
