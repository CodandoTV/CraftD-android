import craftd_swiftui

struct ContentView: View {
    @State var list = [SimpleProperties]()
    var craftBuilders = CraftDBuilders()
    
    var body: some View {
        ScrollView {
            LazyVStack {
                ForEach(list) { item in
                    let builder = craftBuilders.getBuilder(
                        key: item.key
                    )
                    AnyView(erasing: builder.craft(model: item) { _ in })
                }
            }
        }
        .padding()
        .onAppear(perform: {
            do {
                try loadJson()
            } catch {
                print(error.localizedDescription)
            }
        })
    }
    
    private func loadJson() throws {
        guard let jsonData = Bundle.main.path(forResource: "dynamic", ofType: "json") else {
            return
        }
        let fileURL = URL(fileURLWithPath: jsonData)
        let data = try Data(contentsOf: fileURL)
        let container = try SimplePropertiesContainer(from: data)
        list = container.items
    }
    
    
    
}

#Preview {
    ContentView()
}
