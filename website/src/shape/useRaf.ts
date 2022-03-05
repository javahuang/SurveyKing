import {useLayoutEffect, useState} from "react";

const useRaf = (value:number, ms: number = 1e12): number => {
    const [elapsed, set] = useState<number>(0);

    useLayoutEffect(() => {
        let raf: number;
        let timerStop: NodeJS.Timeout;
        let start: number;

        const onFrame = () => {
            const time = Math.min(1, (Date.now() - start) / ms);
            set(time);
            loop();
        };
        const loop = () => {
            raf = requestAnimationFrame(onFrame);
        };
        const onStart = () => {
            timerStop = setTimeout(() => {
                cancelAnimationFrame(raf);
                set(1);
            }, ms);
            start = Date.now();
            loop();
        };
        const timerDelay = setTimeout(onStart, 0);

        return () => {
            clearTimeout(timerStop);
            clearTimeout(timerDelay);
            cancelAnimationFrame(raf);
        };
    }, [value, ms]);

    return elapsed;
};

export default useRaf;