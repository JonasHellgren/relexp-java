package core.animation;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import lombok.SneakyThrows;

import javax.swing.*;
import java.util.concurrent.Executors;

public record AnimationKit(
        GfxComponentFactory gxfFactory,
        Renderer renderer,
        AnimationSettings settings
) {

    public static AnimationKit empty() {
        return new AnimationKit(null, null, null);
    }

    public static AnimationKit of(GfxComponentFactory factory,
                                  AnimationSettings settings) {
        var renderer = Renderer.of(
                factory.getLineCharts(),
                factory.getHeatMapCharts(),
                factory.getTableModels());
        return new AnimationKit(factory, renderer, settings);
    }

    public void start() {
        if (isEmpty()) return;
        showFrame();
        registerRendererOnEventBus();
    }

    public boolean isEmpty() {
        return gxfFactory == null;
    }


    private void showFrame() {
        if (isEmpty()) return;
        JPanel root = gxfFactory.getRootPanel();
        FrameUtil.showFrame(root,
                settings.frameWidth(),
                settings.frameHeight(),
                settings.frameXLocation(),
                settings.frameYLocation());
    }

    private void registerRendererOnEventBus() {
        var renderer = Renderer.of(
                gxfFactory.getLineCharts(),
                gxfFactory.getHeatMapCharts(),
                gxfFactory.getTableModels());
        var bus = getEventBus();
        bus.register(renderer);
    }

    private static EventBus getEventBus() {
        return new AsyncEventBus(Executors.newSingleThreadExecutor());
    }

    @SneakyThrows
    public void postAndSleep(GraphicsDto dto) {
        if (isEmpty()) return;
        renderer.render(dto);
        Thread.sleep(settings.animationDelay());
    }
}
