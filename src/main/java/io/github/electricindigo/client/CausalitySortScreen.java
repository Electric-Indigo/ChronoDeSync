package io.github.electricindigo.client;

import io.github.electricindigo.research.puzzle.causality.CausalityClue;
import io.github.electricindigo.research.puzzle.causality.CausalityEvent;
import io.github.electricindigo.research.puzzle.causality.CausalityGenerator;
import io.github.electricindigo.research.puzzle.causality.CausalityPuzzle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.*;

public class CausalitySortScreen extends Screen
{

    private static final List<List<String>> STORIES = List.of(
            List.of(
                    "the reactor sparks to life",
                    "researchers evacuate the lab",
                    "temporal shielding fails",
                    "the first anomaly appears",
                    "emergency containment engages",
                    "silence returns to the lab"
            ),
            List.of(
                    "the expedition departs",
                    "supplies run low",
                    "a strange beacon is found",
                    "the guide vanishes",
                    "a distress signal is sent",
                    "rescue arrives too late"
            ),
            List.of(
                    "gears begin turning",
                    "power surges through the core",
                    "the countdown starts",
                    "operators lose control",
                    "the machine stabilizes itself",
                    "a new hum fills the chamber"
            )
    );

    private static final int CARD_WIDTH = 180;
    private static final int CARD_HEIGHT = 20;
    private static final int CARD_GAP = 4;
    private static final int LIST_X = 30;
    private static final int LIST_Y = 40;

    private static final int CLUE_PANEL_X = LIST_X + CARD_WIDTH + 30;
    private static final int CLUE_PANEL_Y = LIST_Y;
    private static final int CLUE_PANEL_WIDTH = 200;
    private static final int CLUE_PANEL_HEIGHT = 140;

    private int scrollOffset = 0;
    private int maxScroll = 0;

    private final CausalityPuzzle puzzle;
    private final List<CausalityEvent> currentOrder;
    private final Map<CausalityEvent, String> flavorText;

    private int draggingIndex = -1;
    private double dragMouseY;

    private String resultMessage = null;
    private boolean resultCorrect = false;

    public CausalitySortScreen(long seed, int difficulty)
    {
        super(Component.literal("Causality Sort"));
        this.puzzle = CausalityGenerator.generate(seed, difficulty);
        this.currentOrder = new ArrayList<>(puzzle.events());
        this.flavorText = buildFlavorText(seed);
    }

    private Map<CausalityEvent, String> buildFlavorText(long seed)
    {
        Random random = new Random(seed);
        List<String> story = new ArrayList<>(STORIES.get(random.nextInt(STORIES.size())));
        Collections.shuffle(story, random);

        Map<CausalityEvent, String> result = new HashMap<>();
        List<CausalityEvent> events = puzzle.events();
        for (int i = 0; i < events.size(); i++)
        {
            result.put(events.get(i), story.get(i));
        }
        return result;
    }

    @Override
    protected void init()
    {
        addRenderableWidget(Button.builder(Component.literal("Submit"), button -> onSubmit())
                .bounds(LIST_X, LIST_Y + currentOrder.size() * (CARD_HEIGHT + CARD_GAP) + 20, 100, 20)
                .build());
    }

    private void onSubmit()
    {
        resultCorrect = puzzle.isCorrect(currentOrder);
        resultMessage = resultCorrect ? "Correct!" : "Not quite - try again.";

        if (resultCorrect)
        {
            Minecraft.getInstance().gui.hud.getChat().addClientSystemMessage(Component.literal(resultMessage));
            this.onClose();
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        for (int i = 0; i < currentOrder.size(); i++)
        {
            if (i == draggingIndex) continue;
            int y = LIST_Y + i * (CARD_HEIGHT + CARD_GAP);
            drawCard(graphics, currentOrder.get(i), LIST_X, y, mouseX, mouseY);
        }
        if (draggingIndex >= 0)
        {
            int y = (int) (dragMouseY - CARD_HEIGHT / 2.0);
            drawCard(graphics, currentOrder.get(draggingIndex), LIST_X, y, mouseX, mouseY);
        }

        int padding = 6;
        int headerHeight = 12;
        int clueLineGap = 10;
        int clueGapBetween = 6;
        int textWrapWidth = CLUE_PANEL_WIDTH - padding * 2;

        List<List<FormattedCharSequence>> perClueLines = new ArrayList<>();
        for (CausalityClue clue : puzzle.clues())
        {
            perClueLines.add(font.split(Component.literal(describeClue(clue)), textWrapWidth));
        }

        int contentHeight = 0;

        for (int c = 0; c < perClueLines.size(); c++)
        {
            contentHeight += perClueLines.get(c).size() * clueLineGap;
            if (c < perClueLines.size() -1) contentHeight += clueGapBetween;
        }

        int viewHeight = CLUE_PANEL_HEIGHT - headerHeight - padding * 2;
        maxScroll = Math.max(0, contentHeight - viewHeight);
        scrollOffset = Math.min(scrollOffset, maxScroll);

        graphics.fill(CLUE_PANEL_X - 1, CLUE_PANEL_Y - 1, CLUE_PANEL_X + CLUE_PANEL_WIDTH + 1, CLUE_PANEL_Y + CLUE_PANEL_HEIGHT + 1, 0xFF3F3F3F);
        graphics.fill(CLUE_PANEL_X, CLUE_PANEL_Y, CLUE_PANEL_X + CLUE_PANEL_WIDTH, CLUE_PANEL_Y + CLUE_PANEL_HEIGHT, 0xC0101010);

        graphics.text(font, "Clues:", CLUE_PANEL_X + padding, CLUE_PANEL_Y + padding - 2, 0xFFFFFFFF);

        int contentTop = CLUE_PANEL_Y + padding + headerHeight;
        int contentBottom = CLUE_PANEL_Y + CLUE_PANEL_HEIGHT - padding;

        graphics.enableScissor(CLUE_PANEL_X, contentTop, CLUE_PANEL_X + CLUE_PANEL_WIDTH, contentBottom);

        int lineY = contentTop - scrollOffset;
        for (int c = 0; c < perClueLines.size(); c++)
        {
            for (var line : perClueLines.get(c))
            {
                graphics.text(font, line, CLUE_PANEL_X + padding, lineY, 0xFFAAAAAA);
                lineY += clueLineGap;
            }
            if (c < perClueLines.size() - 1)
            {
                int dividerY = lineY + clueGapBetween / 2 - 1;
                graphics.fill(CLUE_PANEL_X + padding, dividerY, CLUE_PANEL_X + CLUE_PANEL_WIDTH - padding, dividerY + 1, 0xFF444444);
                lineY += clueGapBetween;
            }
        }

        graphics.disableScissor();

        if (maxScroll > 0)
        {
            int trackX = CLUE_PANEL_X + CLUE_PANEL_WIDTH - 4;
            int trackHeight = contentBottom - contentTop;
            int thumbHeight = Math.max(10, trackHeight * viewHeight / Math.max(1, contentHeight));
            int thumbY = contentTop + (trackHeight - thumbHeight) * scrollOffset / Math.max(1, maxScroll);

            graphics.fill(trackX, contentTop, trackX + 2, contentTop + trackHeight, 0xFF222222);
            graphics.fill(trackX, thumbY, trackX + 2, thumbY + thumbHeight, 0xFFAAAAAA);
        }

        if (resultMessage != null)
        {
            int color = resultCorrect ? 0xFF55FF55 : 0xFFFF5555;
            graphics.text(font, resultMessage, LIST_X, LIST_Y - 20, color);
        }
        super.extractBackground(graphics, mouseX, mouseY, a);
    }

    private void drawCard(GuiGraphicsExtractor graphics, CausalityEvent event, int x, int y, int mouseX, int mouseY)
    {
        boolean hovered = mouseX >= x && mouseX <= x + CARD_WIDTH && mouseY >= y && mouseY <= y + CARD_HEIGHT;
        int bg = hovered ? 0xFF555555 : 0xFF333333;

        graphics.fill(x-1, y-1, x + CARD_WIDTH +1, y + CARD_HEIGHT+1, 0xFF888888);
        graphics.fill(x, y, x + CARD_WIDTH, y + CARD_HEIGHT, bg);

        String label = flavorText.getOrDefault(event, event.id());
        graphics.text(font, label, x + 5, y + 6, 0xFFFFFFFF);
    }

    private String describeClue(CausalityClue clue)
    {
        if (clue instanceof CausalityClue.Before before)
        {
            return flavorText.get(before.first()) + " happens before " + flavorText.get(before.second());
        }
        if (clue instanceof CausalityClue.NotAdjacent notAdjacent)
        {
            return flavorText.get(notAdjacent.a()) + " is not adjacent to " + flavorText.get(notAdjacent.b());
        }
        return "unknown clue";
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick)
    {
        if (event.button() == 1)
        {
            for (int i = 0; i < currentOrder.size(); i++)
            {
                int y = LIST_Y + i * (CARD_HEIGHT + CARD_GAP);
                if (event.x() >= LIST_X && event.x() <= LIST_X + CARD_WIDTH && event.y() >= y && event.y() <= y + CARD_HEIGHT)
                {
                    draggingIndex = i;
                    dragMouseY = event.y();
                    return true;
                }
            }
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy)
    {
        if (draggingIndex >= 0)
        {
            dragMouseY = event.y();
            return true;
        }
        return super.mouseDragged(event, dx, dy);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event)
    {
        if (draggingIndex >= 0)
        {
            int targetIndex = (int) Math.round((event.y() - LIST_Y) / (double) (CARD_HEIGHT + CARD_GAP));
            targetIndex = Math.max(0, Math.min(currentOrder.size() - 1, targetIndex));

            CausalityEvent moved = currentOrder.remove(draggingIndex);
            currentOrder.add(targetIndex, moved);

            draggingIndex = -1;
            resultMessage = null;
            return true;
        }
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scrollX, double scrollY)
    {
        if (x >= CLUE_PANEL_X && x <= CLUE_PANEL_X + CLUE_PANEL_WIDTH && y >= CLUE_PANEL_Y && y <= CLUE_PANEL_Y + CLUE_PANEL_HEIGHT)
        {
            scrollOffset -= (int) (scrollY * 12);
            scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
            return true;
        }
        return super.mouseScrolled(x, y, scrollX, scrollY);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
