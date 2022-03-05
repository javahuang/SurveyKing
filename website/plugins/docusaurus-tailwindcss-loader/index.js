

module.exports = function (context, options) {
    return {
        name: 'postcss-tailwindcss-loader',
        configurePostCss(postcssOptions) {
            const newOptions = {
                ident: 'postcss',
                plugins: [
                    require('postcss-import'),
                    require('tailwindcss'),
                    require('postcss-preset-env')({
                        autoprefixer: {
                            flexbox: 'no-2009',
                        },
                        stage: 4,
                    })
                ],
            };
            return { ...postcssOptions, ...newOptions };
        },
    };
};

