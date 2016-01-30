module.exports = {
  html: {
    init: {
        autoescape: false
    },
    dest: 'html/',
    src: ['swig/*.swig'],
    production: false,
    generateSitemap: false,
    generateRobotstxt: false,
    build: true,
    app: {
      name: 'Materil',
      version: '1.0.2',
      color: {
          primary: '#3f51b5',
          info:    '#2196f3',
          success: '#4caf50',
          warning: '#ffc107',
          danger:  '#f44336',
          accent:  '#7e57c2',
          white:   '#ffffff',
          light:   '#f1f2f3',
          dark:    '#475069'
      },
      setting: {
        theme: {
            primary: 'blue',
            accent: 'green',
            warn: 'amber'
        },
        asideFolded: false
      },
        search: {
          content: '',
          show: false
        }
    },
    showData: true,
    data1: '[ 16,15,15,14,17,18,16,15,16 ]',
    data2: '[ 60,30,10 ]',
    data3: '[ 16,15,15,14,17,18,16,15,16 ]',
    data4: '[ 16,15,15,14,17,18,16,15,16 ]',
    plot_pie: "[{label:'Series 1', data: 45}, {label:'Series 2', data: 5}, {label:'Series 3', data: 30}, {label:'Series 4', data: 20}]",
    plot_line: '[[1, 7.5], [2, 7.5], [3, 5.7], [4, 8.9], [5, 10], [6, 7], [7, 9], [8, 13], [9, 7], [10, 6]]',
    plot_line_1: '[[1, 9.5], [2, 9.4], [3, 9.5], [4, 9.5], [5, 9.7], [6, 9.6], [7, 9.3], [8, 9.5], [9, 9], [10, 9.9]]',
    plot_line_2: '[[1, 4.5], [2, 4.2], [3, 4.5], [4, 4.3], [5, 4.5], [6, 4.7], [7, 4.6], [8, 4.8], [9, 4.6], [10, 4.5]]',
    plot_line_3: '[[1, 14], [2, 5.7], [3, 9.6], [4, 7.8], [5, 6.6], [6, 10.5]]',
    world_markers: "[{latLng: [41.90, 12.45], name: 'Vatican City'}, {latLng: [43.93, 12.46], name: 'San Marino'}, {latLng: [47.14, 9.52], name: 'Liechtenstein'}, {latLng: [7.11, 171.06], name: 'Marshall Islands'}, {latLng: [17.3, -62.73], name: 'Saint Kitts and Nevis'}, {latLng: [3.2, 73.22], name: 'Maldives'}, {latLng: [35.88, 14.5], name: 'Malta'}, {latLng: [12.05, -61.75], name: 'Grenada'}, {latLng: [13.16, -61.23], name: 'Saint Vincent and the Grenadines'}, {latLng: [13.16, -59.55], name: 'Barbados'}, {latLng: [17.11, -61.85], name: 'Antigua and Barbuda'}, {latLng: [-4.61, 55.45], name: 'Seychelles'}, {latLng: [7.35, 134.46], name: 'Palau'}, {latLng: [42.5, 1.51], name: 'Andorra'} ]",
    usa_markers: "[{latLng: [40.71, -74.00], name: 'New York'},{latLng: [34.05, -118.24], name: 'Los Angeles'},{latLng: [41.87, -87.62], name: 'Chicago'},{latLng: [29.76, -95.36], name: 'Houston'},{latLng: [39.95, -75.16], name: 'Philadelphia'},{latLng: [38.90, -77.03], name: 'Washington'}, {latLng: [37.36, -122.03], name: 'Silicon Valley'}]",
    cityAreaData: '[605.16, 310.69, 405.17, 248.31, 207.35, 217.22, 137.70, 280.71, 210.32, 325.42]'
  }
}
