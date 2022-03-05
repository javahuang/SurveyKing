import React, { useEffect, useRef } from "react";
import * as THREE from "three";

// @ts-ignore
import { BlurPass, EffectComposer, RenderPass } from "postprocessing";

const WHITE = new THREE.Color(0xFFFFFF),
    GREY = new THREE.Color(0xCCCCCC),
    RED = new THREE.Color(0xFF0000),
    YELLOW = new THREE.Color(0xFFFF00),
    BLUE = new THREE.Color(0x0000FF),
    CYAN = new THREE.Color(0x00FFFF),
    LIGHT_GREEN = new THREE.Color(0x70C4CE),
    ORANGE = new THREE.Color(0xf66023),
    PURPLE = new THREE.Color(0x590D82),
    MAGENTA = new THREE.Color(0xff00ff),
    PINK = new THREE.Color(0xCE70A5);

const CAMERA_FACTOR = 40;
// const CAMERA_FACTOR = 180;
const TIME_DILATION = 1 / 2;
const BRIGHTNESS = .8;


type SceneState = {
    renderer: THREE.WebGLRenderer,
    camera: THREE.OrthographicCamera,
    scene: THREE.Scene,
    geometry: THREE.BufferGeometry,
    material: THREE.MeshStandardMaterial,
    mesh: THREE.Mesh
}

export function ThreeJSAnimationShader() {

    const canvasRef = useRef<HTMLCanvasElement | null>(null);
    const sceneStateRef = useRef<SceneState | null>(null);

    function initScene(ref: HTMLCanvasElement, width: number, height: number): SceneState {

        const renderer = new THREE.WebGLRenderer({
            // antialias: true,
            // alpha: true,
            canvas: ref
        });

        renderer.setClearColor(0xffffff);
        renderer.setSize(width, height);
        renderer.setPixelRatio(1);


        const scene = new THREE.Scene();

        const geometry = new THREE.SphereBufferGeometry(15,
            60,
            60,
            // 0,
            // Math.PI,
            // Math.PI / 4,
            // Math.PI / 2
        );

        // let geometry = new THREE.DodecahedronGeometry(10, 10);
        console.log("pointsCount", geometry.attributes.position.array.length);

        const material = new THREE.MeshStandardMaterial();

        // material.wireframe = true;
        material.userData.delta = { value: 0 };
        material.userData.brightness = { value: BRIGHTNESS };
        // material.side = THREE.DoubleSide;

        material.onBeforeCompile = function(shader) {
            shader.uniforms.delta = material.userData.delta;
            shader.uniforms.brightness = material.userData.brightness;
            // shader.fragmentShader = "uniform float delta;\n" + shader.fragmentShader;
            shader.fragmentShader = buildFragmentShader();
            shader.vertexShader = buildVertexShader();
        };

        const mesh = new THREE.Mesh(geometry, material);
        scene.add(mesh);

        // BG plane
        const planeGeometry = new THREE.PlaneGeometry(500, 500);
        const plane = new THREE.Mesh(planeGeometry, material);
        plane.position.z = -500;
        scene.add(plane);

        const left = width / -CAMERA_FACTOR ;
        const right = width / CAMERA_FACTOR;
        const top = height / CAMERA_FACTOR;
        const bottom = height / -CAMERA_FACTOR;
        const near = 1;
        const far = 1000;
        const camera = new THREE.OrthographicCamera(left, right, top, bottom, near, far);
        camera.position.z = 500;
        camera.position.y = -13;
        camera.position.x = 2;

        return {
            renderer,
            camera,
            scene,
            geometry,
            material,
            mesh
        };
    }

    function setSize(state: SceneState, width: number, height: number) {
        console.log(state);
        state.renderer.setSize(width, height);
        state.camera.left = width / -CAMERA_FACTOR;
        state.camera.right = width / CAMERA_FACTOR;
        state.camera.top = height / CAMERA_FACTOR;
        state.camera.bottom = height / -CAMERA_FACTOR;
        state.camera.updateProjectionMatrix();
    }

    useEffect(() => {

        if (!canvasRef.current)
            return;

        const width = canvasRef.current.offsetWidth,
            height = canvasRef.current.offsetHeight;

        const sceneState = initScene(canvasRef.current, width, height);
        sceneStateRef.current = sceneState;
        const {
            renderer,
            camera,
            scene,
            material
        } = sceneState;

        const clock = new THREE.Clock();

        //RENDER LOOP
        render();

        function render() {
            // const delta = clock.getDelta();
            // mesh.rotation.y += delta * 0.05 * (1) % Math.PI;
            // mesh.rotation.x += delta * 0.05 * (-1) % Math.PI;
            material.userData.delta.value = clock.getElapsedTime() * TIME_DILATION;
            material.userData.brightness.value = BRIGHTNESS;
            // updateLightsPosition(delta, light, light2, light4, light5, mesh);
            renderer.render(scene, camera);
            requestAnimationFrame(render);
        }

        return () => {
            // Callback to cleanup three js, cancel animationFrame, etc
        };
    }, [canvasRef.current]);

    useEffect(() => {
        function handleResize() {
            if (sceneStateRef.current && canvasRef.current) {
                console.log("resizing");
                const width = window.innerWidth,
                    height = window.innerHeight;
                canvasRef.current.width= width;
                setSize(sceneStateRef.current, width, height);
            }
        }

        window.addEventListener("resize", handleResize);
        return () => window.removeEventListener("resize", handleResize);
    }, [window.innerHeight, window.innerWidth]);

    return <canvas
        className={"h-screen w-screen"}
        ref={canvasRef}
    />;

}

function buildVertexShader() {
    return `
    uniform float delta;

    varying vec2 vUv;
    varying vec3 vNormal;
    varying vec3 vPosition;
    varying float v_displacement_amount;

    vec3 mod289(vec3 x)
    {
      return x - floor(x * (1.0 / 289.0)) * 289.0;
    }

    vec4 mod289(vec4 x)
    {
      return x - floor(x * (1.0 / 289.0)) * 289.0;
    }

    vec4 permute(vec4 x)
    {
      return mod289(((x*34.0)+1.0)*x);
    }

    vec4 taylorInvSqrt(vec4 r)
    {
      return 1.79284291400159 - 0.85373472095314 * r;
    }

    vec3 fade(vec3 t) {
      return t*t*t*(t*(t*6.0-15.0)+10.0);
    }

    // Classic Perlin noise
    float cnoise(vec3 P)
    {
      vec3 Pi0 = floor(P); // Integer part for indexing
      vec3 Pi1 = Pi0 + vec3(1.0); // Integer part + 1
      Pi0 = mod289(Pi0);
      Pi1 = mod289(Pi1);
      vec3 Pf0 = fract(P); // Fractional part for interpolation
      vec3 Pf1 = Pf0 - vec3(1.0); // Fractional part - 1.0
      vec4 ix = vec4(Pi0.x, Pi1.x, Pi0.x, Pi1.x);
      vec4 iy = vec4(Pi0.yy, Pi1.yy);
      vec4 iz0 = Pi0.zzzz;
      vec4 iz1 = Pi1.zzzz;

      vec4 ixy = permute(permute(ix) + iy);
      vec4 ixy0 = permute(ixy + iz0);
      vec4 ixy1 = permute(ixy + iz1);

      vec4 gx0 = ixy0 * (1.0 / 7.0);
      vec4 gy0 = fract(floor(gx0) * (1.0 / 7.0)) - 0.5;
      gx0 = fract(gx0);
      vec4 gz0 = vec4(0.5) - abs(gx0) - abs(gy0);
      vec4 sz0 = step(gz0, vec4(0.0));
      gx0 -= sz0 * (step(0.0, gx0) - 0.5);
      gy0 -= sz0 * (step(0.0, gy0) - 0.5);

      vec4 gx1 = ixy1 * (1.0 / 7.0);
      vec4 gy1 = fract(floor(gx1) * (1.0 / 7.0)) - 0.5;
      gx1 = fract(gx1);
      vec4 gz1 = vec4(0.5) - abs(gx1) - abs(gy1);
      vec4 sz1 = step(gz1, vec4(0.0));
      gx1 -= sz1 * (step(0.0, gx1) - 0.5);
      gy1 -= sz1 * (step(0.0, gy1) - 0.5);

      vec3 g000 = vec3(gx0.x,gy0.x,gz0.x);
      vec3 g100 = vec3(gx0.y,gy0.y,gz0.y);
      vec3 g010 = vec3(gx0.z,gy0.z,gz0.z);
      vec3 g110 = vec3(gx0.w,gy0.w,gz0.w);
      vec3 g001 = vec3(gx1.x,gy1.x,gz1.x);
      vec3 g101 = vec3(gx1.y,gy1.y,gz1.y);
      vec3 g011 = vec3(gx1.z,gy1.z,gz1.z);
      vec3 g111 = vec3(gx1.w,gy1.w,gz1.w);

      vec4 norm0 = taylorInvSqrt(vec4(dot(g000, g000), dot(g010, g010), dot(g100, g100), dot(g110, g110)));
      g000 *= norm0.x;
      g010 *= norm0.y;
      g100 *= norm0.z;
      g110 *= norm0.w;
      vec4 norm1 = taylorInvSqrt(vec4(dot(g001, g001), dot(g011, g011), dot(g101, g101), dot(g111, g111)));
      g001 *= norm1.x;
      g011 *= norm1.y;
      g101 *= norm1.z;
      g111 *= norm1.w;

      float n000 = dot(g000, Pf0);
      float n100 = dot(g100, vec3(Pf1.x, Pf0.yz));
      float n010 = dot(g010, vec3(Pf0.x, Pf1.y, Pf0.z));
      float n110 = dot(g110, vec3(Pf1.xy, Pf0.z));
      float n001 = dot(g001, vec3(Pf0.xy, Pf1.z));
      float n101 = dot(g101, vec3(Pf1.x, Pf0.y, Pf1.z));
      float n011 = dot(g011, vec3(Pf0.x, Pf1.yz));
      float n111 = dot(g111, Pf1);

      vec3 fade_xyz = fade(Pf0);
      vec4 n_z = mix(vec4(n000, n100, n010, n110), vec4(n001, n101, n011, n111), fade_xyz.z);
      vec2 n_yz = mix(n_z.xy, n_z.zw, fade_xyz.y);
      float n_xyz = mix(n_yz.x, n_yz.y, fade_xyz.x);
      return 2.2 * n_xyz;
    }

    void main() {

        vUv = uv;

        float s = 1.35;
        float r = delta * 0.25;

        vNormal = normal;
        vPosition = position;

        float displacement = cnoise(s * normal + r) / 3.0 + 1.0;
        v_displacement_amount = displacement;
        vec3 newPosition = position  * displacement ;
        gl_Position = projectionMatrix * modelViewMatrix * vec4( newPosition, 1.0 );
        // gl_Position = projectionMatrix * modelViewMatrix * vec4( position, 1.0 );
    }
`;
}

// https://zz85.github.io/glsl-optimizer/
function buildFragmentShaderOptimized() {

    return `
uniform float delta;
uniform float brightness;
varying vec3 vNormal;
varying vec3 vPosition;
varying float v_displacement_amount;
void main ()
{
  vec3 hsv_1;
  float tmpvar_2;
  float tmpvar_3;
  tmpvar_3 = (v_displacement_amount / 3.0);
  tmpvar_2 = (((
    abs((((
      ((sin((
        (vPosition.x / 4.0)
       + vNormal.x)) + (sin(
        ((vPosition.y / 3.0) + vNormal.y)
      ) * 2.0)) + (sin((
        (vPosition.z / 4.0)
       + vNormal.z)) * 2.0))
     +
      sin(tmpvar_3)
    ) + (
      sin((delta / 2.0))
     * 4.0)) + 10.0))
   / 20.0) * 0.65) + 0.35);
  vec3 tmpvar_4;
  tmpvar_4.x = tmpvar_2;
  tmpvar_4.y = ((abs(
    sin((tmpvar_3 + (delta / 7.0)))
  ) * 0.2) + 0.55);
  tmpvar_4.z = ((abs(
    cos((tmpvar_3 + (delta / 3.0)))
  ) * 0.3) + brightness);
  hsv_1.yz = tmpvar_4.yz;
  hsv_1.x = (tmpvar_2 + 0.05);
  vec4 tmpvar_5;
  tmpvar_5.w = 1.0;
  tmpvar_5.xyz = (tmpvar_4.z * mix (vec3(1.0, 1.0, 1.0), clamp (
    (abs(((
      fract((hsv_1.xxx + vec3(1.0, 0.6666667, 0.3333333)))
     * 6.0) - vec3(3.0, 3.0, 3.0))) - vec3(1.0, 1.0, 1.0))
  , 0.0, 1.0), tmpvar_4.y));
  gl_FragColor = tmpvar_5;
}
    `;
}

function buildFragmentShader() {

    return `
			uniform float delta;
			uniform float brightness;

			varying vec2 vUv;
			varying vec3 vNormal;
            varying vec3 vPosition;
            varying float v_displacement_amount;

            vec3 hsv2rgb(vec3 c)
            {
                vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
                vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
                return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
            }

			void main( void ) {

                float h =
                        (abs(
                        sin(vPosition.x / 4.0 + vNormal.x)
                        + sin(vPosition.y / 3.0 + vNormal.y) * 2.0
                        + sin(vPosition.z / 4.0 + vNormal.z) * 2.0
                        + sin(v_displacement_amount / 3.0)
                        + sin(delta / 2.0) * 4.0
                        ) )
                          / 11.0 * 0.65 + .35;

                float s = abs(sin(v_displacement_amount / 3.0 + delta / 7.0)) * 0.2 + .55;
                float v = abs(cos(v_displacement_amount / 3.0 + delta / 3.0 )) * 0.3 + brightness;
                vec3 hsv = vec3(h,s,v);
                hsv.x = hsv.x + .05;
                gl_FragColor = vec4( hsv2rgb(hsv), 1.0 );

			}
    `;


}


