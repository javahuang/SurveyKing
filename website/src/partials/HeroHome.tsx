import React, { Suspense, useState } from "react";
import HeroButtons from "./HeroButtons";
import BrowserOnly from "@docusaurus/BrowserOnly";

// @ts-ignore
import darkModeVideo from "@site/static/img/preview.mp4";
import HeaderTyper from "./HeaderTyper";

const LazyThreeJSAnimationShader = React.lazy(
  () => import("../shape/ThreeJSAnimationShader")
);

function HeroHome({}) {
  const fallback = (
    <canvas
      style={{
        height: "1000px",
        width: "100vh",
        maxHeight: "1000px",
        position: "fixed",
        transform: `translateY(60px)`,
        top: 0,
        zIndex: -10,
      }}
    />
  );

  const video = (
    <div
      data-aos="fade-up"
      data-aos-delay="400"
      className="xl:max-w-3xl px-8 sm:px-16 md:px-24 xl:px-4"
    >
      <video
        className={"rounded-xl border-gray-200 shadow-3xl"}
        width="100%"
        loop
        autoPlay
        muted
      >
        <source src={darkModeVideo} type="video/mp4" />
      </video>
    </div>
  );

  const [toTypeWords] = useState<string[]>([
    "功能更强大的",
    "更稳定的",
    "更专业的",
    "更易用的",
    "部署更简单的",
  ]);

  const titleDiv = (
    <div className="sm:px-6 px-16 my-4 xl:my-16">
      <div className="text-center xl:text-right">
        <h1 className="block text-5xl md:text-6xl font-extrabold tracking-tight leading-none">
          <span
            data-aos="zoom-y-out"
            data-aos-delay="50"
            className="block"
          ></span>
          <span data-aos="zoom-y-out" data-aos-delay="100" className="block">
            <HeaderTyper
              words={toTypeWords}
              delay={5000}
              defaultText={toTypeWords[0] || "simple"}
            />
          </span>

          <span
            data-aos="zoom-y-out"
            data-aos-delay="150"
            style={{
              // mixBlendMode: "color-dodge",
              WebkitBackgroundClip: "text",
              backgroundClip: "text",
              backgroundImage:
                "linear-gradient(to right, #EC4C51, #FA5574, #9543C1, #9543C1)",
            }}
            className="text-8xl font-extrabold text-8xl md:text-9xl text-transparent bg-clip-text"
          >
            问卷系统
          </span>
        </h1>

        <div className="mt-8 mb-16 flex justify-center xl:justify-end">
          <HeroButtons />
        </div>
      </div>
    </div>
  );

  return (
    <section
      className="relative"
      style={{
        isolation: "isolate",
      }}
    >
      <BrowserOnly fallback={fallback}>
        {() => <Suspense fallback={fallback}></Suspense>}
      </BrowserOnly>

      <div className="xl:grid xl:grid-cols-2 xl:my-24 md:my-16 my-8 ">
        {titleDiv}

        {video}
      </div>
    </section>
  );
}

export default HeroHome;
